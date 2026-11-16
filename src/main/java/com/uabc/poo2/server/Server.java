package com.uabc.poo2.server;

import com.uabc.poo2.modelo.Mensaje;
import java.io.*;
import java.net.*;

/**
 * Server
 */
public class Server extends Thread { // esta vaina usa la clase Mensaje pero como no la tengo se la voy poniendo mientras
                                     // espero jale cuando hagas el pull o si no me mato
  private static final int PUERTO_TCP = 1234; // Puerto TCP pa la partida
  private boolean running = true;

  @Override
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(PUERTO_TCP)) {
      System.out.println("Servidor iniciado en el puerto " + PUERTO_TCP + ". esperando jugador/retador...");

      //el servidor se queda bloqueado aquí hasta que un cliente se conecte
      Socket socketCliente = serverSocket.accept();
      System.out.println("Conexion establecida con el cliente: " + socketCliente.getInetAddress());

      //los canales de comunicación por objetos con el objectstream
      try (
          ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
          ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream())
      ) {
        //mensaje de bienvenida de prueba
        Mensaje bienvenida = new Mensaje("CONEXION_EXITOSA", "Servidor", "bienvenido a la partida de pokemon card-jitsu");
        oos.writeObject(bienvenida);
        oos.flush();

        // ciclo para escuchar los mesajes o jugadas del cliente durante el juego
        while (running) {
          Mensaje mensajeRecibido = (Mensaje) ois.readObject();
          if (mensajeRecibido != null) {
            System.out.println("mensaje recibido del cliente: " + mensajeRecibido);

            // Aquí después pondremos la lógica de cuando el cliente tire una carta etc etc
            // Por ahora solo imprime el mensaje recibido
            if ("SELECCIONAR_CARTA".equals(mensajeRecibido.getTipo())) {
              System.out.println("el jugador tiro la carta: " + mensajeRecibido.getCarta());
            }
          }
        }

      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

    } catch (IOException e) {
      System.err.println("Error en el servidor: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void detener() {
    this.running = false;
  }
}