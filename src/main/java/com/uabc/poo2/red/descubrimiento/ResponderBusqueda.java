package com.uabc.poo2.red.descubrimiento;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.DatagramPacket;

import com.uabc.poo2.red.protocolo.Protocolo;

/**
 * ResponderBusqueda
 */

public class ResponderBusqueda extends Thread {
  private final DatagramSocket socket;
  private volatile boolean running = true;
  private String serverName;

  public ResponderBusqueda(String serverName) throws SocketException {
    socket = new DatagramSocket(Protocolo.PUERTO_UDP); // aquí saltaría el BindException
    this.serverName = serverName;
  }

  @Override
  public void run() {
    // Iniciamos el servidor
    try {

      byte[] buffer = new byte[256];

      while (running) {
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length); // Especificamos como tinee que ser el
                                                                            // paquete
        socket.receive(paquete); // Se queda esperando recibir un mensaje

        String mensaje = new String(paquete.getData(), 0, paquete.getLength(), Protocolo.CHARSET); // Cuando llega, lo volvemos String

        if (mensaje.equals(Protocolo.PETICION_BUSQUEDA)) { // Vemos si es el mensaje que esperabamos

          String respuesta = (Protocolo.RESPUESTA_BUSQUEDA + Protocolo.SEPARADOR + serverName + Protocolo.SEPARADOR
              + Protocolo.PUERTO_TCP);
          // Le mandamos EY LO ENCONTRASTE (luego el ponemos algo mejor),
          // nombre de la sala (tmb se ocupa cambiar luego como por el
          // nombre de usuario o algo asi) yy el puerto del servidor TCP

          byte[] datos = respuesta.getBytes(Protocolo.CHARSET); // Lo volvemos bytes

          DatagramPacket paqueteRespuesta = new DatagramPacket(datos, datos.length, paquete.getAddress(),
              paquete.getPort()); // Enviamos el mensaje solo a quien nos mando la peticion
          socket.send(paqueteRespuesta);

        }
      }

    } catch (IOException e) {
      if (running) // Si se cierra mientras esta corriendo es un error
        e.printStackTrace();
    } finally {
      socket.close();
    }
  }

  public void detener() {
    running = false;
    socket.close();
  }

}
