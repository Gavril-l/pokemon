package com.uabc.poo2.persistencia;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.query.Query;

import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.util.Passwords;
import com.uabc.poo2.util.Utilidades;

import javafx.scene.control.Alert;

/**
 * RepositorioPerfiles
 */

// Es casi lo mismo que la practica 3
public class RepositorioPerfiles {

  private Path ruta;
  public ObjectContainer db;
  private static RepositorioPerfiles instancia;

  public static RepositorioPerfiles getInstancia() {

    if (instancia == null) {
      instancia = new RepositorioPerfiles();
    }

    return instancia;
  }

  private RepositorioPerfiles() {

    try {

      ruta = Paths.get("datos", "Perfiles.db4o");

      ruta.getParent().toFile().mkdirs();

      if (db == null || db.ext().isClosed()) {
        db = Db4oEmbedded.openFile(ruta.toString());
        crearAdmin();
      }

    } catch (DatabaseFileLockedException e) {
      Utilidades.alertar("Error en base de datos", "La base de datos se encuentra bloqueada por otro proceso",
          Alert.AlertType.ERROR);
      e.printStackTrace();

    } catch (Exception e) {
      Utilidades.alertar("Error en base de datos", "Hubo un error al generar lo necesario para la base de datos",
          Alert.AlertType.ERROR);
      e.printStackTrace();
    }
  }

  public void cerrar() {
    if (db != null) {
      db.close();
    }
  }

  // No creo que en algun punto ocupemos buscar pero x
  public ObjectSet<Perfil> busqueda(String nombre) {

    Query query = db.query();
    query.constrain(Perfil.class);
    query.descend("nombre").constrain(nombre);

    ObjectSet<Perfil> usuarios_object = query.execute();

    return usuarios_object;
  }

  public Perfil autentificar(String nombre, String contraseña_plana) {

    Query query = db.query();
    query.constrain(Perfil.class);

    String contraseña = Passwords.generarHash(contraseña_plana);

    query.descend("nombre").constrain(nombre);
    query.descend("contraseña").constrain(contraseña);

    ObjectSet<Perfil> user = query.execute();
    if (!user.isEmpty()) {
      return user.next();
    }

    return null;

  }

  public void agregarUsuario(Perfil perfil) {
    try {
      db.store(perfil);
      db.commit();

      Utilidades.alertar("Creacion del usuario", "El usuario" + perfil.getNombre() + " se ha creado con exito",
          Alert.AlertType.CONFIRMATION);
    } catch (Exception e) {
      if (db != null)
        db.rollback();
      Utilidades.alertar("Error del sistema", "No se ha podido crear el usuario", Alert.AlertType.ERROR);
      e.printStackTrace();
    }
  };

  private void crearAdmin() {
    Query query = db.query();
    query.constrain(Perfil.class);

    ObjectSet<Perfil> usuarios = query.execute();

    if (usuarios.isEmpty()) {
      Perfil administrador = new Perfil("admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
      db.store(administrador);
      db.commit();
      System.out.println("Administrador creado exitosamente");
    }
  }

  public ObjectSet<Perfil> listar() {
    Query query = db.query();
    query.constrain(Perfil.class);
    ObjectSet<Perfil> perfiles = query.execute();
    return perfiles;
  }

}
