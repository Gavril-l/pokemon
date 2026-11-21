package com.uabc.poo2.red;

import com.uabc.poo2.red.descubrimiento.ResponderBusqueda;
import com.uabc.poo2.red.protocolo.Mensaje;
import com.uabc.poo2.red.protocolo.Protocolo;
import com.uabc.poo2.red.protocolo.TipoDeMensaje;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Server
 */
public class Server extends Thread {
  private volatile boolean running = true;
  private volatile ServerSocket serverSocket;
  private volatile Socket socketCliente;
  private volatile ResponderBusqueda responder;
  private String serverName;
  private Runnable onClienteConectado;
  private Consumer<String> onError;

  public Server(String serverName) {
    this.serverName = serverName;
  }

  public void setOnClienteConectado(Runnable onClienteConectado) {
    this.onClienteConectado = onClienteConectado;
  }

  /**
   * Se llama (desde el hilo del servidor, no del de JavaFX) cuando el servidor no
   * pudo iniciar o se cayo. Usa Platform.runLater si vas a tocar la interfaz.
   **/
  public void setOnError(Consumer<String> onError) {
    this.onError = onError;
  }

  public String getServerName() {
    return serverName;
  }

  private void reportarError(String mensaje) {
    System.err.println(mensaje);
    if (onError != null) {
      onError.accept(mensaje);
    }
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

        // Avisar al controlador del host que ya entró el oponente
        if (onClienteConectado != null) {
          onClienteConectado.run();
        }

        // ciclo para escuchar los mesajes o jugadas del cliente durante el juego
        while (running) {
          Mensaje mensajeRecibido = (Mensaje) ois.readObject();
          if (mensajeRecibido != null) {
            System.out.println("mensaje recibido del cliente: " + mensajeRecibido);

            if (TipoDeMensaje.SELECCIONAR_CARTA.equals(mensajeRecibido.getTipo())) {
              System.out.println("el jugador tiro la carta: " + mensajeRecibido.getPokemon());
            }
          }
        }

      } catch (EOFException e) {
        System.err.println("El cliente se desconecto");

      } catch (ClassNotFoundException e) {
        if (running)
          e.printStackTrace();
      }
    } catch (BindException e) {
      // puerto TCP o UDP ocupado (otro servidor corriendo en esta misma maquina)
      if (running)
        reportarError("No se pudo iniciar la sala: el puerto ya esta en uso. " + e.getMessage());
    } catch (IOException e) {
      if (running) {
        e.printStackTrace();
        reportarError("Error en el servidor: " + e.getMessage());
      }
    } finally {
      if (responder != null)
        responder.detener();
    }

  }

  public void detener() {
    this.running = false;
    try {
      if (responder != null)
        responder.detener();
      if (serverSocket != null)
        serverSocket.close();
      if (socketCliente != null)
        socketCliente.close();
    } catch (IOException e) {
    }
  }
}
