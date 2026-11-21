
package com.uabc.poo2.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Jugador
 */

// No tiene tanto que ver con lo otro que estaba haciendo, pero puede que te
// facilite el trabajo con el juego, aqui ya
// se guarda su mano y todo (cosas que deberia hacer el catalogo).

public class Jugador implements Serializable {

  private static final long serialVersionUID = 1L;

  private String nombre;
  private int puntos;
  private List<Pokemon> mano = new ArrayList<>();

  public Jugador() {
  };

  public Jugador(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getPuntos() {
    return puntos;
  }

  public void setPuntos(int puntos) {
    this.puntos = puntos;
  }

  public List<Pokemon> getMano() {
    return mano;
  }

  public void setMano(List<Pokemon> mano) {
    this.mano = mano;
  }

}
