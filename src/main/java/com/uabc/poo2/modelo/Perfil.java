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

  public Perfil() {
    idPokemonFavorito = "001";
  }

  public Perfil(String nombre, String contraseña) {
    this();
    this.nombre = nombre;
    this.contraseña = contraseña;
  }

  public String getContraseña() {
    return contraseña;
  };

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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getIdPokemonFavorito() {
    return idPokemonFavorito;
  }

  public void setIdPokemonFavorito(String idPokemonFavorito) {
    this.idPokemonFavorito = idPokemonFavorito;
  };

}
