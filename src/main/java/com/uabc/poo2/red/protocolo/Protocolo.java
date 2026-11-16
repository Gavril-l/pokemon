package com.uabc.poo2.red.protocolo;

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

  public static final int TIMEOUT_BUSQUEDA_MS = 1500;
}
