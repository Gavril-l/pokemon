package com.uabc.poo2.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * HistorialLucha
 */
public class HistorialLucha implements Serializable {
  private static final long serialVersionUID = 1L;

  public static class RegistroPelea implements Serializable {
    private static final long serialVersionUID = 1L;

    private String rival;
    private String resultado;
    private String fecha;

    public RegistroPelea() {
    }

    public RegistroPelea(String rival, String resultado, String fecha) {
      this.rival = rival;
      this.resultado = resultado;
      this.fecha = fecha;
    }

    public String getRival() {
      return rival;
    }

    public String getResultado() {
      return resultado;
    }

    public String getFecha() {
      return fecha;
    }
  }

  private int victorias;
  private int derrotas;
  private List<RegistroPelea> registros;

  public HistorialLucha() {
    this.victorias = 0;
    this.derrotas = 0;
    this.registros = new ArrayList<>();
  }

  public void registrarVictoria(String rival) {
    this.victorias++;
    agregarRegistro(rival, "Victoria");
  }

  public void registrarDerrota(String rival) {
    this.derrotas++;
    agregarRegistro(rival, "Derrota");
  }

  private void agregarRegistro(String rival, String resultado) {
    if (this.registros == null) {
      this.registros = new ArrayList<>();
    }
    String rivalLimpio = (rival == null || rival.isBlank()) ? "Oponente" : rival;
    String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    this.registros.add(new RegistroPelea(rivalLimpio, resultado, fechaActual));
  }

  public int getVictorias() {
    return victorias;
  }

  public void setVictorias(int victorias) {
    this.victorias = victorias;
  }

  public int getDerrotas() {
    return derrotas;
  }

  public void setDerrotas(int derrotas) {
    this.derrotas = derrotas;
  }

  public List<RegistroPelea> getRegistros() {
    if (this.registros == null) {
      this.registros = new ArrayList<>();
    }
    return this.registros;
  }

  public void setRegistros(List<RegistroPelea> registros) {
    this.registros = registros;
  }

  public int getTotalPeleas() {
    return victorias + derrotas;
  }

  public double getPorcentajeVictorias() {
    int total = getTotalPeleas();
    if (total == 0) {
      return 0.0;
    }
    return (victorias * 100.0) / total;
  }
}