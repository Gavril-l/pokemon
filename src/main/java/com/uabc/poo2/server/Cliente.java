package com.uabc.poo2.server;

import com.uabc.poo2.modelo.Mensaje;
import com.uabc.poo2.server.descubrimiento.ServidorEncontrado;
import java.io.*;
import java.net.*;

public class Cliente { // esta vaina usa la clase Mensaje pero como no la tengo se la voy poniendo mientras
                        // espero jale cuando hagas el pull o si no me mato
  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  //metodo para conectar usando el ServidorEncontrado del UDP que pusiste
  public void conectar(ServidorEncontrado servidor) {
    conectar(servidor.getDireccion(), servidor.getPuertoTcp());
  }

  //metodo para conectar usando InetAddress y puerto
  public void conectar(InetAddress ipServidor, int puerto) {
    try {
      System.out.println("conectando al servidor en " + ipServidor.getHostAddress() + ":" + puerto + "...");
      socket = new Socket(ipServidor, puerto);
      
      // los canales de comunicación por objetos con el objectstream
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      System.out.println("conectado exitosamente al servidor");

      // para leer el mensaje de bienvenida del servidor
      Mensaje bienvenida = (Mensaje) ois.readObject();
      if (bienvenida != null) {
        System.out.println("Mensaje del servidor: " + bienvenida.getContenido());
      }

    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Error al conectar con el servidor: " + e.getMessage());
      e.printStackTrace();
    }
  }

  // sobrecarga para mantener compatibilidad si le pasas la IP como string
  public void conectar(String ipServidor, int puerto) {
    try {
      conectar(InetAddress.getByName(ipServidor), puerto);
    } catch (UnknownHostException e) {
      System.err.println("Dirección IP inválida: " + e.getMessage());
      e.printStackTrace();
    }
  }

  // metodo para enviar mensajes o jugadas (como tirar una carta) al servidor
  public void enviarMensaje(Mensaje mensaje) {
    try {
      if (oos != null) {
        oos.writeObject(mensaje);
        oos.flush();
        System.out.println("Mensaje enviado al servidor: " + mensaje);
      }
    } catch (IOException e) {
      System.err.println("Error al enviar mensaje: " + e.getMessage());
      e.printStackTrace();
    }
  }

  //pa cerrar la conexión con el servidor
  public void desconectar() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
        System.out.println("Desconectado del servidor...");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}