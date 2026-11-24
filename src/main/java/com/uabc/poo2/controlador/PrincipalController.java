package com.uabc.poo2.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

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
    FXMLLoader loader = Utilidades.cambiarPantalla(event, "MatchMaking - Pokémon Card-Jitsu", "servers.fxml");
    if (loader == null) {
      return;
    }

    ServersPageControlador controlador = loader.getController();
    if (cliente == null) {
      cliente = new Cliente();
    }
    controlador.setCliente(cliente);
    if (servidor != null) {
      controlador.setServidor(servidor);
    }
  }

  @FXML
  void handleEstadisticas(ActionEvent event) {
    Utilidades.cambiarPantalla(event, "Estadísticas - Pokémon Card-Jitsu", "estadisticas.fxml");
  }

  @FXML
  void handleSalir(ActionEvent event) {
    if (cliente != null) {
      cliente.desconectar();
    }
    if (servidor != null) {
      servidor.detener();
    }
    Platform.exit();
  }

  @FXML
  void handleCatalogo(ActionEvent event) {
    Utilidades.cambiarPantalla(event, "Catalogo", "catalogo.fxml");
  }
}