package com.uabc.poo2.persistencia;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.query.Query;

import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.util.Passwords;

/**
 * RepositorioPerfiles
 */

public class RepositorioPerfiles {

  private static final int PROFUNDIDAD_ACTUALIZACION = 5;
  private static final int PROFUNDIDAD_ACTIVACION = 10;
  private static final int INTENTOS_APERTURA = 200; // ~5 a 8 segundos en total
  private static final long ESPERA_BASE_MS = 15;

  private static final Path RUTA = Paths.get("datos", "Perfiles.db4o");
  private static final Object CANDADO = new Object();

  private static RepositorioPerfiles instancia;
  private boolean adminVerificado;

  @FunctionalInterface
  private interface Operacion<T> {
    T ejecutar(ObjectContainer db);
  }

  public static synchronized RepositorioPerfiles getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioPerfiles();
    }
    return instancia;
  }

  public static void cerrarSiExiste() {
  }

  public void cerrar() {
  }

  private RepositorioPerfiles() {
  }

  private ObjectContainer abrir() {
    RUTA.getParent().toFile().mkdirs();

    for (int intento = 1;; intento++) {
      try {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().updateDepth(PROFUNDIDAD_ACTUALIZACION);
        config.common().activationDepth(PROFUNDIDAD_ACTIVACION);
        return Db4oEmbedded.openFile(config, RUTA.toString());
      } catch (DatabaseFileLockedException e) {
        if (intento >= INTENTOS_APERTURA) {
          throw new BaseDeDatosException("La base de datos se encuentra bloqueada por otro proceso", e);
        }
        try {
          // pequeño azar para que dos instancias no choquen siempre al mismo tiempo
          Thread.sleep(ESPERA_BASE_MS + ThreadLocalRandom.current().nextLong(25));
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          throw new BaseDeDatosException("Se interrumpio la espera de la base de datos", ie);
        }
      } catch (RuntimeException e) {
        throw new BaseDeDatosException("No se pudo abrir la base de datos: " + e.getMessage(), e);
      }
    }
  }

  // abre, ejecuta, confirma y cierra (siempre cierra)
  private <T> T conBD(Operacion<T> operacion) {
    synchronized (CANDADO) {
      ObjectContainer db = abrir();
      try {
        if (!adminVerificado) {
          crearAdminSiHaceFalta(db);
        }
        T resultado = operacion.ejecutar(db);
        db.commit();
        adminVerificado = true;
        return resultado;
      } catch (BaseDeDatosException e) {
        rollbackSilencioso(db);
        throw e;
      } catch (RuntimeException e) {
        rollbackSilencioso(db);
        throw new BaseDeDatosException("Error en la base de datos: " + e.getMessage(), e);
      } finally {
        db.close();
      }
    }
  }

  private void rollbackSilencioso(ObjectContainer db) {
    try {
      db.rollback();
    } catch (RuntimeException ignorada) {
    }
  }

  private void crearAdminSiHaceFalta(ObjectContainer db) {
    if (db.query(Perfil.class).isEmpty()) {
      db.store(new Perfil("admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"));
      System.out.println("Administrador creado exitosamente");
    }
  }

  // devuelve el perfil ya activado (con todos sus datos) o null si no existe
  private Perfil buscarEnBD(ObjectContainer db, String nombre) {
    Query query = db.query();
    query.constrain(Perfil.class);
    query.descend("nombre").constrain(nombre);
    ObjectSet<Perfil> encontrados = query.execute();
    if (encontrados.isEmpty()) {
      return null;
    }
    Perfil perfil = encontrados.next();
    db.activate(perfil, PROFUNDIDAD_ACTIVACION);
    return perfil;
  }

  public List<Perfil> listar() {
    return conBD(db -> {
      List<Perfil> perfiles = new ArrayList<>();
      for (Perfil p : db.query(Perfil.class)) {
        db.activate(p, PROFUNDIDAD_ACTIVACION);
        perfiles.add(p);
      }
      return perfiles;
    });
  }

  public boolean existe(String nombre) {
    return conBD(db -> buscarEnBD(db, nombre) != null);
  }

  public Perfil autentificar(String nombre, String contrasenaPlana) {
    String hash = Passwords.generarHash(contrasenaPlana);
    return conBD(db -> {
      Perfil perfil = buscarEnBD(db, nombre);
      if (perfil != null && hash.equals(perfil.getContraseña())) {
        return perfil;
      }
      return null;
    });
  }

  /** @return false si ya existe un usuario con ese nombre (la revision y el guardado son una sola operacion) */
  public boolean agregarUsuario(Perfil perfil) {
    return conBD(db -> {
      if (buscarEnBD(db, perfil.getNombre()) != null) {
        return false;
      }
      db.store(perfil);
      return true;
    });
  }

  /**
   * Suma una victoria o derrota al perfil que esta EN la base de datos y devuelve
   * ese perfil ya actualizado (para ponerlo en la Sesion). Asi no se crean
   * duplicados y tampoco se pisan los cambios que haya hecho otra instancia.
   */
  public Perfil registrarResultado(String nombre, boolean victoria, String rival) {
    return conBD(db -> {
      Perfil perfil = buscarEnBD(db, nombre);
      if (perfil == null) {
        throw new BaseDeDatosException("No existe el usuario \"" + nombre + "\" en la base de datos");
      }
      if (victoria) {
        perfil.registrarVictoriaEnPartida(rival);
      } else {
        perfil.registrarDerrotaEnPartida(rival);
      }
      db.store(perfil);
      db.store(perfil.getHistorialLucha());
      db.store(perfil.getHistorialLucha().getRegistros());
      return perfil;
    });
  }
}