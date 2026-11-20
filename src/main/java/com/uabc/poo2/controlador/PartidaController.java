package com.uabc.poo2.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    // al salir de la partida se cierra todo, si no los puertos 1234 (TCP) y
    //  4445 (UDP) se quedan ocupados y no se puede volver a crear una sala.
    if (cliente != null) {
      cliente.desconectar();
    }
    if (servidor != null) {
      servidor.detener();
    }

    FXMLLoader loader = Utilidades.cambiarPantalla(event, "MatchMaking - Pokémon Card-Jitsu", "servers.fxml");
    if (loader == null) {
      return;
    }

    // Cliente nuevo: el anterior ya esta desconectado
    ServersPageControlador controlador = loader.getController();
    controlador.setCliente(new Cliente());
  }
}