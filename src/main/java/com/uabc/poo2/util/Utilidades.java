package com.uabc.poo2.util;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * Utilidades
 */

public class Utilidades {

  private static final String RUTA_FXML = "/com/uabc/poo2/vista/";

  public static FXMLLoader cambiarPantalla(ActionEvent event, String titulo, String recurso) {
    return cambiarPantalla((Node) event.getSource(), titulo, recurso);
  }

  // igual que la linea de arriba pero para cuando no hay ActionEvent (por ejemplo si se
  // llama desde un callback o un hilo). devuelve null si no pudo cargar la vista.
  public static FXMLLoader cambiarPantalla(Node origen, String titulo, String recurso) {

    try {

      FXMLLoader fxmlLoader = new FXMLLoader(Utilidades.class.getResource(RUTA_FXML + recurso));
      Parent raiz = fxmlLoader.load();
      Scene anterior = origen.getScene();
      Stage stage = (Stage) anterior.getWindow();

      double minAncho = Math.max(0, raiz.minWidth(-1));
      double minAlto = Math.max(0, raiz.minHeight(-1));

      //la ventana conserva el tamaño que ya tenia (si el usuario la agrando o la
      //maximizo, la pantalla nueva se acomoda a ese tamaño) y solo crece si la
      //pantalla nueva necesita mas espacio. Los bordes de la ventana se suman al minimo.
      double bordeAncho = stage.getWidth() - anterior.getWidth();
      double bordeAlto = stage.getHeight() - anterior.getHeight();
      double ancho = Math.max(anterior.getWidth(), minAncho);
      double alto = Math.max(anterior.getHeight(), minAlto);

      stage.setScene(new Scene(raiz, ancho, alto));
      stage.setMinWidth(minAncho + bordeAncho);
      stage.setMinHeight(minAlto + bordeAlto);
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