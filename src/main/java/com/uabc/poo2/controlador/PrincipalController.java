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
import javafx.stage.Stage;

import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.red.Server;
import com.uabc.poo2.util.Utilidades;

public class PrincipalController {

  @FXML
  private Button btnJugar;

  @FXML
  private Button btnEstadisticas;

  @FXML
  private Button btnSalir;

  private Cliente cliente;
  private Server servidor;

  public void setConexion(Cliente cliente, Server servidor) {
    this.cliente = cliente;
    this.servidor = servidor;
  }

  @FXML
  void handleJugar(ActionEvent event) {
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

      Stage stage = (Stage) btnJugar.getScene().getWindow();
      stage.setScene(new Scene(root));
      stage.setTitle("MatchMaking - Pokémon Card-Jitsu");
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      Utilidades.alertar("Error de vista", "No se pudo abrir servers.fxml: " + e.getMessage(),
          Alert.AlertType.ERROR);
    }
  }

  @FXML
  void handleEstadisticas(ActionEvent event) {
    Utilidades.alertar("Estadísticas", "Sección de estadísticas en construcción.", Alert.AlertType.INFORMATION);
  }

  @FXML
  void handleSalir(ActionEvent event) {
    if (cliente != null) {
      cliente.desconectar();
    }
    Platform.exit();
  }
}