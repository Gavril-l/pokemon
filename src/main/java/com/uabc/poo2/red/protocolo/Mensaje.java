package com.uabc.poo2.red.protocolo;

import java.io.Serializable;
import com.uabc.poo2.modelo.Pokemon;

/**
 * Mensaje
 */
public class Mensaje implements Serializable {
  private static final long serialVersionUID = 1L;

  private TipoDeMensaje tipo; // ejemplo -> "CONEXION_EXITOSA", "SELECCIONAR_CARTA"
  private String remitente; // es el "quien manda el mensaje" (servidor o nombre del jugador)
  private String contenido; // texto plano o información general
  private Pokemon pokemon; // el objeto pokemon o pokémon que se esté tirando en la partida
  private Pokemon pokemonOponente; // carta del rival para revelar cuando ambos elijan
  private int rondasGanadas; // total de rondas ganadas acumuladas
  private int victoriasMismoTipo; // mayor racha/conteo de victorias con el mismo tipo

  public Mensaje() {
  }

  public Mensaje(TipoDeMensaje tipo, String remitente, String contenido) {
    this.tipo = tipo;
    this.remitente = remitente;
    this.contenido = contenido;
  }

  public Mensaje(TipoDeMensaje tipo, String remitente, String contenido, Pokemon pokemon) {
    this.tipo = tipo;
    this.remitente = remitente;
    this.contenido = contenido;
    this.pokemon = pokemon;
  }

  public TipoDeMensaje getTipo() {
    return tipo;
  }

  public void setTipo(TipoDeMensaje tipo) {
    this.tipo = tipo;
  }

  public String getRemitente() {
    return remitente;
  }

  public void setRemitente(String remitente) {
    this.remitente = remitente;
  }

  public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }

  public Pokemon getPokemon() {
    return pokemon;
  }

  public void setPokemon(Pokemon pokemon) {
    this.pokemon = pokemon;
  }

  public Pokemon getPokemonOponente() {
    return pokemonOponente;
  }

  public void setPokemonOponente(Pokemon pokemonOponente) {
    this.pokemonOponente = pokemonOponente;
  }

  public int getRondasGanadas() {
    return rondasGanadas;
  }

  public void setRondasGanadas(int rondasGanadas) {
    this.rondasGanadas = rondasGanadas;
  }

  public int getVictoriasMismoTipo() {
    return victoriasMismoTipo;
  }

  public void setVictoriasMismoTipo(int victoriasMismoTipo) {
    this.victoriasMismoTipo = victoriasMismoTipo;
  }

  @Override
  public String toString() {
    return "Mensaje [tipo=" + tipo + ", remitente=" + remitente + ", contenido=" + contenido + ", pokemon=" + pokemon
        + ", pokemonOponente=" + pokemonOponente + "]";
  }
}