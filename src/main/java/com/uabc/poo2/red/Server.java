package com.uabc.poo2.red;

import com.uabc.poo2.red.descubrimiento.ResponderBusqueda;
import com.uabc.poo2.red.protocolo.Mensaje;
import com.uabc.poo2.red.protocolo.Protocolo;
import com.uabc.poo2.red.protocolo.TipoDeMensaje;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
 */
public class Server extends Thread { // esta vaina usa la clase Mensaje pero como no la tengo se la voy poniendo
                                     // mientras
                                     // espero jale cuando hagas el pull o si no me mato
  private volatile boolean running = true;
  private volatile ServerSocket serverSocket;
  private volatile Socket socketCliente;
  private volatile ResponderBusqueda responder;
  private String serverName;

  public Server(String serverName) {
    this.serverName = serverName;
  }

  @Override
  public void run() {

    try (ServerSocket ss = new ServerSocket(Protocolo.PUERTO_TCP)) {
      serverSocket = ss;

      responder = new ResponderBusqueda(serverName);
      responder.setDaemon(true);
      responder.start();

      System.out.println("Servidor iniciado en el puerto " + Protocolo.PUERTO_TCP + ". esperando jugador/retador...");

      // el servidor se queda bloqueado aquí hasta que un cliente se conecte
      socketCliente = serverSocket.accept();
      responder.detener();

      System.out.println("Conexion establecida con el cliente: " + socketCliente.getInetAddress());

      // los canales de comunicación por objetos con el objectstream
      try (
          ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
          ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream())) {
        // mensaje de bienvenida de prueba
        Mensaje bienvenida = new Mensaje(TipoDeMensaje.CONEXION_EXITOSA, "Servidor",
            "bienvenido a la partida de pokemon card-jitsu");
        oos.writeObject(bienvenida);
        oos.flush();

        // ciclo para escuchar los mesajes o jugadas del cliente durante el juego
        while (running) {
          Mensaje mensajeRecibido = (Mensaje) ois.readObject();
          if (mensajeRecibido != null) {
            System.out.println("mensaje recibido del cliente: " + mensajeRecibido);

            // Aquí después pondremos la lógica de cuando el cliente tire una carta etc etc
            // Por ahora solo imprime el mensaje recibido
            if (TipoDeMensaje.SELECCIONAR_CARTA.equals(mensajeRecibido.getTipo())) {
              System.out.println("el jugador tiro la carta: " + mensajeRecibido.getPokemon());
            }
          }
        }

      } catch (EOFException e) {
        System.err.println("El cliente se desconecto" + e.getMessage());

      } catch (ClassNotFoundException e) {
        if (running)
          e.printStackTrace();
      }
    } catch (IOException e) {
      if (running) {
        System.err.println("Error en el servidor: " + e.getMessage());
        e.printStackTrace();
      }
    } finally {
      if (responder != null)
        responder.detener();
    }

  }

  public void detener() {
    this.running = false;
    try {
      if (serverSocket != null)
        serverSocket.close();
      if (socketCliente != null)
        socketCliente.close();
    } catch (IOException e) {
    }
  }
}
