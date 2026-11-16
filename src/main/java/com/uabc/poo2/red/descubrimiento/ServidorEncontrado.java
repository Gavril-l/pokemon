package com.uabc.poo2.red.descubrimiento;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * ServidorEncontrado
 */
public class ServidorEncontrado implements Serializable {
  private String nombre;
  private InetAddress direccion;
  private int puertoTcp;

  public ServidorEncontrado() {
  }

  public ServidorEncontrado(String nombre, InetAddress direccion, int puertoTcp) {
    this.nombre = nombre;
    this.direccion = direccion;
    this.puertoTcp = puertoTcp;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public InetAddress getDireccion() {
    return direccion;
  }

  public void setDireccion(InetAddress direccion) {
    this.direccion = direccion;
  }

  public int getPuertoTcp() {
    return puertoTcp;
  }

  public void setPuertoTcp(int puertoTcp) {
    this.puertoTcp = puertoTcp;
  }

}
