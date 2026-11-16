package com.uabc.poo2.controlador;

import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.util.Utilidades;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PrincipalController {

  @FXML
  private Button btnJugar;

  @FXML
  private Button btnEstadisticas;

  @FXML
  private Button btnSalir;

  @FXML
  private void handleJugar(ActionEvent event) {
    System.out.println("Redirigiendo a sala de espera/matchmaking..."); // esto no hace nada aun pero es un placeholder
                                                                        // para la funcionalidad de matchmaking
    ServersPageControlador controlador = Utilidades.cambiarPantalla(event, "MatchMaking", "servers.fxml")
        .getController();
    controlador.setCliente(new Cliente());

  }

  @FXML
  private void handleEstadisticas(ActionEvent event) {
    System.out.println("Abriendo panel de estadísticas..."); // esto tambien no hace nada aun jeje pero es un
                                                             // placeholder para la funcionalidad de estadísticas, wow
                                                             // vs code escribio todo eso por mi, que grande
  }

  @FXML
  private void handleSalir(ActionEvent event) {
    Stage stage = (Stage) btnSalir.getScene().getWindow();
    stage.close();
  }
} // test
