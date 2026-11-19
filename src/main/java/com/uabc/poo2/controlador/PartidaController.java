package com.uabc.poo2.controlador;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.red.Server;
import com.uabc.poo2.util.Utilidades;

public class PartidaController {

  @FXML
  private Label lblSala;

  @FXML
  private Label lblEstado;

  @FXML
  private Button btnVolver;

  private Cliente cliente;
  private Server servidor;
  private String nombreSala;

  public void inicializarPartida(Cliente cliente, Server servidor, String nombreSala) {
    this.cliente = cliente;
    this.servidor = servidor;
    this.nombreSala = nombreSala;

    if (lblSala != null) {
      lblSala.setText("Sala: " + nombreSala);
    }
    if (lblEstado != null) {
      String rol = (servidor != null) ? "Host + Cliente conectado" : "Cliente conectado";
      lblEstado.setText("Estado: " + rol);
    }
  }

  @FXML
  void onVolverClick(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uabc/poo2/vista/servers.fxml"));
      Parent root = loader.load();

      ServersPageControlador controlador = loader.getController();
      if (cliente == null) {
        cliente = new Cliente();
      }
      controlador.setCliente(cliente);
      if (servidor != null) {
        controlador.setServidor(servidor);
      }

      Stage stage = (Stage) btnVolver.getScene().getWindow();
      stage.setScene(new Scene(root));
      stage.setTitle("MatchMaking - Pokémon Card-Jitsu");
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      Utilidades.alertar("Error de vista", "No se pudo volver a servers.fxml: " + e.getMessage(),
          Alert.AlertType.ERROR);
    }
  }
}