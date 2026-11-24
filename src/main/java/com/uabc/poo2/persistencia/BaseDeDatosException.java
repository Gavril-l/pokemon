package com.uabc.poo2.persistencia;

/**
 * Error al usar la base de datos. Es unchecked para no ensuciar las firmas, y
 * el repositorio NO muestra alertas (eso le toca al controlador): asi se puede
 * llamar desde un hilo normal sin tronar.
 */
public class BaseDeDatosException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public BaseDeDatosException(String mensaje) {
    super(mensaje);
  }

  public BaseDeDatosException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}