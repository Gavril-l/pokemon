package com.uabc.poo2.sesion;

import com.uabc.poo2.modelo.Perfil;

// La idea de sesion es tener un solo punto de acceso al perfil/usuario activo, de aqui de una podemos sacar el nombre
// para el jugador ya en partida

public final class Sesion {
  private static volatile Perfil perfilActivo;

  private Sesion() {
  }

  public static void iniciar(Perfil perfil) {
    perfilActivo = perfil;
  }

  public static void cerrar() {
    perfilActivo = null;
  }

  public static boolean hayPerfil() {
    return perfilActivo != null;
  }

  public static Perfil getPerfil() {
    if (perfilActivo == null) {
      throw new IllegalStateException("No hay un perfil activo");
    }
    return perfilActivo;
  }
}
