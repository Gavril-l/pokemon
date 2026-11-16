package com.uabc.poo2.server.descubrimiento;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BuscarServidores
 */

public class BuscarServidores {

  private static final int PUERTO_UDP = 4445; // Puerto nomas para buscar
  private static final String PETICION = "BUSCANDO"; // Mensaje que vamos a mandar

  public static List<ServidorEncontrado> buscar() throws IOException {
    List<ServidorEncontrado> encontrados = new ArrayList<>();

    // Iniciamos un servidor
    try (DatagramSocket socket = new DatagramSocket()) {
      socket.setBroadcast(true); // Para que si se mande un mensaje a todos
      socket.setSoTimeout(1500); // Tiempo limite de respuesta

      byte[] bytes = PETICION.getBytes(); // Solo se pueden mandar bytes
      // El InetAddres es como, la direccion esa para todos que mencione en el commit
      DatagramPacket peticion = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("255.255.255.255"),
          PUERTO_UDP);
      socket.send(peticion);// Mandamos el mensaje

      byte[] buffer = new byte[256];

      while (true) {
        DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
        try {
          socket.receive(respuesta); // Esperamos una respuesta que se vea asi
        } catch (SocketTimeoutException e) {
          e.printStackTrace();
          break;
        }

        String mensaje = new String(respuesta.getData(), 0, respuesta.getLength());
        String[] partes = mensaje.split(";"); // Si llega la respuesta, lo convierto en String y la parto en ; para ver
                                              // que dice

        if (partes.length == 3 && partes[0].equals("ENCONTRASTE")) { // Para confirmar que haya sido nuestro mensaje

          ServidorEncontrado server = new ServidorEncontrado(partes[1], respuesta.getAddress(),
              Integer.parseInt(partes[2]));

          encontrados.add(server);

          // Lo agregamos a la lista, con direccion ip y
          // cositas para saber a donde conectarlos ya bien
          // con el servidor TCP como en la practica 9
          //
        }
      }
    }
    return encontrados; // Regresamos todos los encontrados
  }

}
