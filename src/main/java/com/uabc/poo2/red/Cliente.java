package com.uabc.poo2.red;

import com.uabc.poo2.red.protocolo.Mensaje;
import com.uabc.poo2.red.protocolo.Protocolo;
import com.uabc.poo2.red.descubrimiento.ServidorEncontrado;
import com.uabc.poo2.red.descubrimiento.BuscarServidores;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Cliente {
  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private volatile boolean escuchando = false;
  private Thread hiloEscucha;
  private Consumer<Mensaje> onMensajeRecibido;
  private volatile List<ServidorEncontrado> servidoresDisponibles = new ArrayList<>();

  public void setOnMensajeRecibido(Consumer<Mensaje> onMensajeRecibido) {
    this.onMensajeRecibido = onMensajeRecibido;
  }

  public void iniciarEscucha() {
    if (escuchando || ois == null || socket == null || socket.isClosed()) {
      return;
    }
    escuchando = true;
    hiloEscucha = new Thread(() -> {
      try {
        while (escuchando && socket != null && !socket.isClosed()) {
          Object obj = ois.readObject();
          if (obj instanceof Mensaje mensaje && onMensajeRecibido != null) {
            onMensajeRecibido.accept(mensaje);
          }
        }
      } catch (EOFException | SocketException e) {
        if (escuchando) {
          System.out.println("Conexion cerrada durante la partida.");
        }
      } catch (IOException | ClassNotFoundException e) {
        if (escuchando) {
          e.printStackTrace();
        }
      } finally {
        escuchando = false;
      }
    }, "Cliente-Escucha-Partida");
    hiloEscucha.setDaemon(true);
    hiloEscucha.start();
  }

  public List<ServidorEncontrado> getServidoresDisponibles() {
    return servidoresDisponibles;
  }

  public void setServidoresDisponibles(List<ServidorEncontrado> servidoresDisponibles) {
    this.servidoresDisponibles = servidoresDisponibles;
  }

  // metodo para conectar usando el ServidorEncontrado del UDP
  public boolean conectar(ServidorEncontrado servidor) {
    return conectar(servidor.getDireccion(), servidor.getPuertoTcp());
  }

  // metodo para conectar usando InetAddress y puerto
  public boolean conectar(InetAddress ipServidor, int puerto) {
    try {
      System.out.println("conectando al servidor en " + ipServidor.getHostAddress() + ":" + puerto + "...");

      // con timeout, si no el new Socket(ip, puerto) se puede quedar colgado
      socket = new Socket();
      socket.connect(new InetSocketAddress(ipServidor, puerto), Protocolo.TIMEOUT_CONEXION_MS);

      // los canales de comunicación por objetos con el objectstream
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      System.out.println("conectado exitosamente al servidor");

      // para leer el mensaje de bienvenida del servidor (con limite de espera)
      socket.setSoTimeout(Protocolo.TIMEOUT_BIENVENIDA_MS);
      Mensaje bienvenida = (Mensaje) ois.readObject();
      socket.setSoTimeout(0); // ya en partida se espera sin limite

      if (bienvenida != null) {
        System.out.println("Mensaje del servidor: " + bienvenida.getContenido());
      }
      return true;

    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Error al conectar con el servidor: " + e.getMessage());
      e.printStackTrace();
      desconectar();
      return false;
    }
  }

  // sobrecarga para mantener compatibilidad si le pasas la IP como string
  public boolean conectar(String ipServidor, int puerto) {
    try {
      return conectar(InetAddress.getByName(ipServidor), puerto);
    } catch (UnknownHostException e) {
      System.err.println("Dirección IP inválida: " + e.getMessage());
      e.printStackTrace();
      return false;
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

  // pa cerrar la conexión con el servidor
  public void desconectar() {
    escuchando = false;
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
        System.out.println("Desconectado del servidor...");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Esto se va a usar en cuanto se cree el cliente para buscar los servidores
  public void buscarServidores() {
    try {
      servidoresDisponibles = BuscarServidores.buscar();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}