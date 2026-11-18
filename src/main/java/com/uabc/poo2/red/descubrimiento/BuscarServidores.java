package com.uabc.poo2.red.descubrimiento;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import com.uabc.poo2.red.protocolo.Protocolo;

/**
 * BuscarServidores
 */

public class BuscarServidores {

  public static List<ServidorEncontrado> buscar() throws IOException {
    List<ServidorEncontrado> encontrados = new ArrayList<>();

    // Iniciamos un servidor
    try (DatagramSocket socket = new DatagramSocket()) {
      socket.setBroadcast(true); // Para que si se mande un mensaje a todos
      socket.setSoTimeout(Protocolo.TIMEOUT_BUSQUEDA_MS); // Tiempo limite de respuesta

      byte[] bytes = Protocolo.PETICION_BUSQUEDA.getBytes(); // Solo se pueden mandar bytes
      // El InetAddres es como, la direccion esa para todos que mencione en el commit

      // Mandamos el datagrama a todas las direcciones de broadcast
      for (InetAddress broadcast : getDireccionesBroadcast()) {
        try {
          socket.send(new DatagramPacket(bytes, bytes.length, broadcast, Protocolo.PUERTO_UDP));
          System.out.println(broadcast.getHostAddress()); // TODO: Esto lo vamos a quitar luego
        } catch (IOException e) {
          System.err.println("No se pudo enviar a " + broadcast.getHostAddress() + ": " + e.getMessage());
        }
      }

      byte[] buffer = new byte[256];

      while (true) {

        DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
        try {
          socket.receive(respuesta); // Esperamos una respuesta que se vea asi
        } catch (SocketTimeoutException e) {
          break;
        }

        // TODO: Descomentar antes de entrega
        // if (respuesta.getAddress().isLoopbackAddress()
        // || NetworkInterface.getByInetAddress(respuesta.getAddress()) != null)
        // continue;

        String mensaje = new String(respuesta.getData(), 0, respuesta.getLength());
        String[] partes = mensaje.split(Protocolo.SEPARADOR); // Si llega la respuesta, lo convierto en String y la
                                                              // parto en ; para ver que dice

        if (partes.length == 3 && partes[0].equals(Protocolo.RESPUESTA_BUSQUEDA)) { // Para confirmar que haya sido
                                                                                    // nuestro mensaje

          ServidorEncontrado server = new ServidorEncontrado(partes[1], respuesta.getAddress(),
              Integer.parseInt(partes[2]));

          encontrados.add(server);

          // Lo agregamos a la lista, con direccion ip y
          // cositas para saber a donde conectarlos ya bien
          // con el servidor TCP como en la practica 9
        }
      }
    }
    return encontrados; // Regresamos todos los encontrados
  }

  private static List<InetAddress> getDireccionesBroadcast() throws SocketException {
    List<InetAddress> broadcastList = new ArrayList<>();
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    // Obtenemos todas las interfaces, que son como puntos de comunicacion (wifi,
    // eternet y cosas asi) que tienen ip, y si tiene broadcast.

    // interfaces queda en un Enumeration raro, lo pasamos a lista normal
    for (NetworkInterface netIf : Collections.list(interfaces)) {
      if (!netIf.isUp() || netIf.isLoopback()) {
        // Si esta apagado o es la de comunicacion con uno mismo nos saltamos esa
        // interfaz.
        continue;
      }

      for (InterfaceAddress ifAddr : netIf.getInterfaceAddresses()) {
        InetAddress broadcast = ifAddr.getBroadcast();
        // Ahora si sacamos la direccion de broadcast

        if (broadcast != null) {
          // no todas las interfaces tienen direccion de broadcast
          broadcastList.add(broadcast);
        }
      }
    }
    return broadcastList;
  }

}
