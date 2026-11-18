package com.uabc.poo2.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.red.Server;
import com.uabc.poo2.red.descubrimiento.ServidorEncontrado;
import com.uabc.poo2.util.Utilidades;

public class ServersPageControlador {

  // TODO: rediseñar la pantalla del MatchMaking
  private Cliente cliente;
  private Server servidor;
  private Thread hiloBusqueda;

  @FXML
  private Button btnIniciarServidor;

  @FXML
  private ScrollPane spServidoresDisponibles;

  @FXML
  private TextField txtSala;

  @FXML
  void onIniciarServidorClick(ActionEvent event) {
    if (txtSala.getText().isBlank() || txtSala.getText().isEmpty()) {
      Utilidades.alertar("Error de sala", "Ingresa un nombre de sala valido", Alert.AlertType.WARNING);
      return;
    }

    servidor = new Server(txtSala.getText());
    servidor.setDaemon(true);
    servidor.start();
    btnIniciarServidor.setDisable(true);
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
    hiloBusqueda = new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        cliente.buscarServidores();
        Platform.runLater(() -> {
          actualizarServidores();
        });
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          return;
        }
      }
    });

    hiloBusqueda.setDaemon(true);
    hiloBusqueda.start();

  }

  private void actualizarServidores() {
    VBox vbServer = new VBox(20);

    for (ServidorEncontrado server : cliente.getServidoresDisponibles()) {
      HBox contenedor = new HBox(20);
      Label nombre = new Label(server.getNombre());
      Button btnConectar = new Button("Conectar");
      contenedor.getChildren().add(nombre);
      contenedor.getChildren().add(btnConectar);
      vbServer.getChildren().add(contenedor);
    }

    spServidoresDisponibles.setContent(vbServer);
  }

  public void detenerBusqueda() {
    if (hiloBusqueda != null)
      hiloBusqueda.interrupt();
  }

}
