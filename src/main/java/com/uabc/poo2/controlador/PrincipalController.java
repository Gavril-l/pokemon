package com.uabc.poo2.controlador;

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
    private void handleJugar() {
        System.out.println("Redirigiendo a sala de espera/matchmaking..."); //esto no hace nada aun pero es un placeholder para la funcionalidad de matchmaking
    }

    @FXML
    private void handleEstadisticas() {
        System.out.println("Abriendo panel de estadísticas..."); //esto tambien no hace nada aun pero es un placeholder para la funcionalidad de estadísticas, wow vs code escribio todo eso por mi, que grande
    }

    @FXML
    private void handleSalir() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }
}