package com.uabc.poo2.modelo;

import java.io.Serializable;

public class Pokemon implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;
  private String nombre;
  private Tipo tipo;
  private int valor; // rango de 1 a 10 o mas segun la carta pokemon o evolucion o no se jejej
  private String color;
  private String rutaImagen;

  public Pokemon() {
  }

  public Pokemon(String id, String nombre, Tipo tipo, int valor, String color, String rutaImagen) {
    this.id = id;
    this.nombre = nombre;
    this.tipo = tipo;
    this.valor = valor;
    this.color = color;
    this.rutaImagen = rutaImagen; // para las imagenes de las cartas, que no hare yo bro
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Tipo getTipo() {
    return tipo;
  }

  public void setTipo(Tipo tipo) {
    this.tipo = tipo;
  }

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getRutaImagen() {
    return rutaImagen;
  }

  public void setRutaImagen(String rutaImagen) {
    this.rutaImagen = rutaImagen;
  }

public Efectividad relacionCon(Tipo atacante) {
  if (this.tipo == null || atacante == null) {
    return Efectividad.NEUTRAL;
  }
  if (tipo.getResistencias().contains(atacante))
    return Efectividad.RESISTE;

  if (tipo.getDebilidades().contains(atacante))
    return Efectividad.DEBIL;

  if (tipo.getInmunidades().contains(atacante))
    return Efectividad.INMUNE;

  return Efectividad.NEUTRAL;
}

  @Override
  public String toString() {
    return "Pokemon{" +
        "nombre='" + nombre + '\'' +
        ", tipo=" + (tipo != null ? tipo.getNombre() : "sin tipo") +
        ", valor=" + valor +
        ", color='" + color + '\'' +
        '}';
  }
}
