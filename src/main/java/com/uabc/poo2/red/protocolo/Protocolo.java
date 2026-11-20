package com.uabc.poo2.red.protocolo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Protocolo
 */
public class Protocolo {

  private Protocolo() {
  };

  public static final int PUERTO_UDP = 4445;
  public static final int PUERTO_TCP = 1234;

  public static final String PETICION_BUSQUEDA = "BUSCANDO";
  public static final String RESPUESTA_BUSQUEDA = "ENCONTRASTE";
  public static final String SEPARADOR = ";";

  // usar UTF-8 para evitar problemas con acentos y ñ entre distintos sistemas operativos.
  public static final Charset CHARSET = StandardCharsets.UTF_8;

  public static final int MAX_NOMBRE_SALA = 30;

  public static final int TIMEOUT_BUSQUEDA_MS = 1500;
  public static final int TIMEOUT_CONEXION_MS = 3000;
  public static final int TIMEOUT_BIENVENIDA_MS = 5000;

  public static String validarNombreSala(String nombre) {
    if (nombre == null || nombre.isBlank())
      return "Ingresa un nombre de sala valido";
    if (nombre.length() > MAX_NOMBRE_SALA)
      return "El nombre de sala no puede pasar de " + MAX_NOMBRE_SALA + " caracteres";
    if (nombre.contains(SEPARADOR))
      return "El nombre de sala no puede contener '" + SEPARADOR + "'";
    return null;
  }
}