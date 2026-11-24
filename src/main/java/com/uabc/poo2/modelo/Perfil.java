package com.uabc.poo2.modelo;

import java.io.Serializable;
/**
 * Perfil
 */
// Para tener un registro de los usuarios
public class Perfil implements Serializable {
  private static final long serialVersionUID = 1L;

  private String idPokemonFavorito;
  private String nombre;
  private String contraseña;
  private int victorias;
  private int derrotas;
  private HistorialLucha historialLucha;

  public Perfil() {
    idPokemonFavorito = "001";
    historialLucha = new HistorialLucha();
  }

  public Perfil(String nombre, String contraseña) {
    this();
    this.nombre = nombre;
    this.contraseña = contraseña;
  }

  public HistorialLucha getHistorialLucha() {
    if (historialLucha == null) {
      historialLucha = new HistorialLucha();
      historialLucha.setVictorias(this.victorias);
      historialLucha.setDerrotas(this.derrotas);
    }
    return historialLucha;
  }

  public void setHistorialLucha(HistorialLucha historialLucha) {
    this.historialLucha = historialLucha;
    if (historialLucha != null) {
      this.victorias = historialLucha.getVictorias();
      this.derrotas = historialLucha.getDerrotas();
    }
  }

  public void registrarVictoriaEnPartida(String rival) {
    HistorialLucha h = getHistorialLucha();
    h.registrarVictoria(rival);
    this.victorias = h.getVictorias();
    this.derrotas = h.getDerrotas();
  }

  public void registrarDerrotaEnPartida(String rival) {
    HistorialLucha h = getHistorialLucha();
    h.registrarDerrota(rival);
    this.victorias = h.getVictorias();
    this.derrotas = h.getDerrotas();
  }

  public String getContraseña() {
    return contraseña;
  }

  public void setContraseña(String contraseña) {
    this.contraseña = contraseña;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getVictorias() {
    return historialLucha != null ? historialLucha.getVictorias() : victorias;
  }

  public void setVictorias(int victorias) {
    this.victorias = victorias;
    getHistorialLucha().setVictorias(victorias);
  }

  public int getDerrotas() {
    return historialLucha != null ? historialLucha.getDerrotas() : derrotas;
  }

  public void setDerrotas(int derrotas) {
    this.derrotas = derrotas;
    getHistorialLucha().setDerrotas(derrotas);
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getIdPokemonFavorito() {
    return idPokemonFavorito;
  }

  public void setIdPokemonFavorito(String idPokemonFavorito) {
    this.idPokemonFavorito = idPokemonFavorito;
  }
}