package com.uabc.poo2.red.descubrimiento;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

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

  //Sirve para que el host no se conecte a su propia sala,
  //ya que el host es el que crea la sala y no se puede conectar a si mismo
  //jeje visual studio me puso lo demas pa que se vea mamalon
  public boolean esLocal() {
    if (direccion == null) {
      return false;
    }
    if (direccion.isLoopbackAddress() || direccion.isAnyLocalAddress()) {
      return true;
    }
    try {
      return NetworkInterface.getByInetAddress(direccion) != null;
    } catch (SocketException e) {
      return false;
    }
  }

}