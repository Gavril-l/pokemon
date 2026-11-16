package com.uabc.poo2.server.descubrimiento;

import java.io.IOException;
import java.net.*;

/**
 * ResponderBusqueda
 */

public class ResponderBusqueda extends Thread {
  private static final int PUERTO_UDP = 4445; // Puerto nomas para buscar
  private static final String PETICION = "BUSCANDO"; // El mensaje que le tiene que llegar

  @Override
  public void run() {
    // Iniciamos el servidor
    try (DatagramSocket socket = new DatagramSocket(PUERTO_UDP)) {

      byte[] buffer = new byte[256];

      while (true) {
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length); // Especificamos como tinee que ser el
                                                                            // paquete
        socket.receive(paquete); // Se queda esperando recibir un mensaje

        String mensaje = new String(paquete.getData(), 0, paquete.getLength()); // Cuando llega, lo volvemos String

        if (mensaje.equals(PETICION)) { // Vemos si es el mensaje que esperabamos

          String respuesta = ("ENCONTRASTE;Sala A;1234"); // Le mandamos EY LO ENCONTRASTE (luego ponemos algo mejor),
                                                          // el
                                                          // nombre de la sala (tmb se ocupa cambiar luego como por el
                                                          // nombre de usuario o algo asi) yy el puerto del servidor TCP
          byte[] datos = respuesta.getBytes(); // Lo volvemos bytes

          DatagramPacket paqueteRespuesta = new DatagramPacket(datos, datos.length, paquete.getAddress(),
              paquete.getPort()); // Enviamos el mensaje solo a quien nos mando la peticion
          socket.send(paqueteRespuesta);

        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
