package com.uabc.poo2.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import com.uabc.poo2.audio.Musica;
import com.uabc.poo2.audio.Musica.Pista;
import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.red.Server;
import com.uabc.poo2.red.descubrimiento.ServidorEncontrado;
import com.uabc.poo2.red.protocolo.Protocolo;
import com.uabc.poo2.util.Utilidades;

public class ServersPageControlador implements Initializable {

  // TODO: rediseñar la pantalla del MatchMaking
  private Cliente cliente;
  private Server servidor;
  private Thread hiloBusqueda;
  private volatile boolean transicionandoAPartida = false;

  @FXML
  private Button btnIniciarServidor;

  @FXML
  private ScrollPane spServidoresDisponibles;

  @FXML
  private TextField txtSala;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Musica.reproducir(Pista.MENU);
  }

  @FXML
  void onIniciarServidorClick(ActionEvent event) {
    String nombreSala = txtSala.getText().trim();
    String error = Protocolo.validarNombreSala(nombreSala);
    if (error != null) {
      Utilidades.alertar("Error de sala", error, Alert.AlertType.WARNING);
      return;
    }

    servidor = new Server(nombreSala);
    servidor.setOnClienteConectado(() -> {
      detenerBusqueda();
      Platform.runLater(() -> cambiarAPantallaPartida(nombreSala, btnIniciarServidor));
    });
    // Si el servidor no puede iniciar (puerto ocupado, etc.) se avisa y se
    // rehabilita el boton
    servidor.setOnError(mensaje -> Platform.runLater(() -> {
      Utilidades.alertar("Error de sala", mensaje, Alert.AlertType.ERROR);
      servidor = null;
      btnIniciarServidor.setDisable(false);
    }));
    servidor.setDaemon(true);
    servidor.start();
    btnIniciarServidor.setDisable(true);
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
    detenerBusqueda();

    hiloBusqueda = new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        cliente.buscarServidores();
        Platform.runLater(() -> actualizarServidores());
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }
      }
    });

    hiloBusqueda.setDaemon(true);
    hiloBusqueda.start();
  }

  public void setServidor(Server servidor) {
    this.servidor = servidor;
  }

  private void actualizarServidores() {
    VBox vbServer = new VBox(20);

    for (ServidorEncontrado server : cliente.getServidoresDisponibles()) {
      // si yo cree una sala, la sala que esta en mi propia computadora es la mia: no
      // se muestra
      if (servidor != null && server.esLocal()) {
        continue;
      }
      HBox contenedor = new HBox(20);
      Label nombre = new Label(server.getNombre());
      Button btnConectar = new Button("Conectar");

      btnConectar.setOnAction(e -> conectarAServidor(server, btnConectar));

      contenedor.getChildren().add(nombre);
      contenedor.getChildren().add(btnConectar);
      vbServer.getChildren().add(contenedor);
    }

    spServidoresDisponibles.setContent(vbServer);
  }

  private void conectarAServidor(ServidorEncontrado server, Button btnConectar) {
    btnConectar.setDisable(true);
    detenerBusqueda();

    Thread hiloConexion = new Thread(() -> {
      boolean conectado = cliente.conectar(server);

      Platform.runLater(() -> {
        if (conectado) {
          cambiarAPantallaPartida(server.getNombre(), btnConectar);
        } else {
          Utilidades.alertar("Error de conexión", "No se pudo conectar a la sala: " + server.getNombre(),
              Alert.AlertType.ERROR);
          btnConectar.setDisable(false);
          if (cliente != null) {
            setCliente(cliente);
          }
        }
      });
    });
    hiloConexion.setDaemon(true);
    hiloConexion.start();
  }

  private void cambiarAPantallaPartida(String nombreSala, javafx.scene.Node nodoOrigen) {
    if (transicionandoAPartida) {
      return;
    }
    transicionandoAPartida = true;

    FXMLLoader loader = Utilidades.cambiarPantalla(nodoOrigen, "Partida - " + nombreSala, "partida.fxml");
    if (loader == null) {
      transicionandoAPartida = false;
      if (nodoOrigen instanceof Button btn) {
        btn.setDisable(false);
      }
      if (cliente != null) {
        setCliente(cliente);
      }
      return;
    }

    Object controlador = loader.getController();
    if (controlador instanceof PartidaController partidaController) {
      partidaController.inicializarPartida(cliente, servidor, nombreSala);
    }
  }

  public void detenerBusqueda() {
    if (hiloBusqueda != null) {
      hiloBusqueda.interrupt();
      hiloBusqueda = null;
    }
  }
}
