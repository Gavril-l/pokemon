package com.uabc.poo2.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import com.uabc.poo2.util.Utilidades;
import com.uabc.poo2.sesion.Sesion;
import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.persistencia.RepositorioPerfiles;

public class InicioController {

  RepositorioPerfiles perfiles = RepositorioPerfiles.getInstancia();

  @FXML
  private Button btnEntrar;

  @FXML
  private Button btnRegistro;

  @FXML
  private TextField txtContraseña;

  @FXML
  private TextField txtUsuario;

  // La neta hice el registro todo rapido (disculpa), esta medio x pero bueno

  @FXML
  void onEntrarClick(ActionEvent event) {

    if (txtUsuario.getText().isBlank() || txtContraseña.getText().isBlank()) {
      Utilidades.alertar("Error de inicio", "Nombre de usuario y contraseña son obligatorios", AlertType.WARNING);
      return;
    }

    Perfil perfil = perfiles.autentificar(txtUsuario.getText(), txtContraseña.getText());

    if (perfil == null) {
      Utilidades.alertar("Base de datos", "Usuario o contraseña incorrectos", AlertType.ERROR);
      return;
    }

    Sesion.iniciar(perfil);
    perfiles.cerrar();
    Utilidades.cambiarPantalla(event, "Pokemon Card-Jitsu", "principal.fxml");
  }

  @FXML
  void onRegistroClick(ActionEvent event) {
    // Esto va a mandar a otra pantalla de registro o asi
  }

}
