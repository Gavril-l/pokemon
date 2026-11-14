package com.uabc.poo2.controlador;

import com.uabc.poo2.modelo.Saludo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrincipalController {

    @FXML
    private Label lblSaludo;

    @FXML
    private Button btnCambiar;

    private final Saludo saludo = new Saludo("Mundo");
    private boolean alternado = false;

    @FXML
    private void initialize() {
        lblSaludo.setText(saludo.getMensaje());
        btnCambiar.setOnAction(e -> alternar());
    }

    private void alternar() {
        alternado = !alternado;
        saludo.setNombre(alternado ? "JavaFX" : "Mundo");
        lblSaludo.setText(saludo.getMensaje());
    }
}
