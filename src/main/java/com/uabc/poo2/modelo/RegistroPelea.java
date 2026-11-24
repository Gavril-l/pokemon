package com.uabc.poo2.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * RegistroPelea
 */
public class RegistroPelea {
  private String rival;
  private boolean victoria;
  private String fecha;

  public RegistroPelea(String rival, boolean victoria) {
    this.rival = rival;
    this.victoria = victoria;
    this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
  }

  public String getRival() {
    return rival;
  }

  public boolean isVictoria() {
    return victoria;
  }

  public String getResultadoTexto() {
    return victoria ? "Victoria" : "Derrota";
  }

  public String getFecha() {
    return fecha;
  }
}