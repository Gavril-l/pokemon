package com.uabc.poo2.controlador;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.red.Server;
import com.uabc.poo2.red.descubrimiento.ServidorEncontrado;
import com.uabc.poo2.util.Utilidades;

public class ServersPageControlador {

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

  @FXML
  void onIniciarServidorClick(ActionEvent event) {
    String nombreSala = txtSala.getText().trim();
    if (nombreSala.isEmpty()) {
      Utilidades.alertar("Error de sala", "Ingresa un nombre de sala valido", Alert.AlertType.WARNING);
      return;
    }

    servidor = new Server(nombreSala);
    servidor.setOnClienteConectado(() -> {
      detenerBusqueda();
      Platform.runLater(() -> cambiarAPantallaPartida(nombreSala, btnIniciarServidor));
    });
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

    new Thread(() -> {
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
    }).start();
  }

  private void cambiarAPantallaPartida(String nombreSala, javafx.scene.Node nodoOrigen) {
    if (transicionandoAPartida) {
      return;
    }
    transicionandoAPartida = true;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uabc/poo2/vista/partida.fxml"));
      Parent root = loader.load();

      Object controlador = loader.getController();
      if (controlador instanceof PartidaController partidaController) {
        partidaController.inicializarPartida(cliente, servidor, nombreSala);
      }

      Stage stage = (Stage) nodoOrigen.getScene().getWindow();
      stage.setScene(new Scene(root));
      stage.setTitle("Partida - " + nombreSala);
      stage.show();
    } catch (IOException e) {
      transicionandoAPartida = false;
      e.printStackTrace();
      Utilidades.alertar("Error de vista", "No se pudo abrir partida.fxml: " + e.getMessage(),
          Alert.AlertType.ERROR);
      if (nodoOrigen instanceof Button btn) {
        btn.setDisable(false);
      }
      if (cliente != null) {
        setCliente(cliente);
      }
    }
  }

  public void detenerBusqueda() {
    if (hiloBusqueda != null) {
      hiloBusqueda.interrupt();
      hiloBusqueda = null;
    }
  }
}