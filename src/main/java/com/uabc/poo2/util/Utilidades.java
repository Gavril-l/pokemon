package com.uabc.poo2.util;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Utilidades
 */

public class Utilidades {

  private static final String RUTA_FXML = "/com/uabc/poo2/vista/";

  public static FXMLLoader cambiarPantalla(ActionEvent event, String titulo, String recurso) {

    try {

      FXMLLoader fxmlLoader = new FXMLLoader(Utilidades.class.getResource(RUTA_FXML + recurso));
      Scene scene = new Scene(fxmlLoader.load());
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      stage.setScene(scene);
      stage.setTitle(titulo);

      return fxmlLoader; // Aqui devuelvo el fxmlLoader para que puedas sacar el controlador si lo ocupas
                         // para hacer un set de algo o yo que se

    } catch (IOException e) {
      alertar("IOException", e.getMessage(), AlertType.ERROR);
      return null;
    }
  }

  // Usa Platform.runLater() si lo vas a usar en hilo
  public static void alertar(String titulo, String texto, AlertType tipo) {

    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);

    if (texto == null || texto.isEmpty())
      alerta.setContentText("Sin informacion");
    else
      alerta.setContentText(texto);

    alerta.showAndWait();
  }
}
