package com.uabc.poo2.controlador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import com.uabc.poo2.util.Passwords;
import com.uabc.poo2.util.Utilidades;
import com.uabc.poo2.sesion.Sesion;

import java.net.URL;
import java.util.ResourceBundle;

import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.persistencia.BaseDeDatosException;
import com.uabc.poo2.persistencia.RepositorioPerfiles;
import com.uabc.poo2.vista.componentes.TarjetaPerfil;

public class InicioController implements Initializable {

  // getInstancia() ya no abre la base: se abre solo durante cada operacion
  private final RepositorioPerfiles repositorio = RepositorioPerfiles.getInstancia();

  @FXML
  private VBox vbPrincipal;

  @FXML
  private FlowPane fpPerfiles;

  @FXML
  private ScrollPane spPerfiles;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    spPerfiles.setFitToWidth(true);

    try {
      for (Perfil p : repositorio.listar()) {
        vbPrincipal.getChildren().add(crearTarjeta(p));
      }
    } catch (BaseDeDatosException e) {
      // runLater: no se puede hacer showAndWait mientras se esta cargando la pantalla
      Platform.runLater(() -> Utilidades.alertar("Error en base de datos", e.getMessage(), Alert.AlertType.ERROR));
    }

    vbPrincipal.getChildren().add(crearTarjeta(null));
  }

  private TarjetaPerfil crearTarjeta(Perfil existente) {
    TarjetaPerfil tarjeta = new TarjetaPerfil(existente);

    if (existente != null) {
      tarjeta.setAlEntrar((perfil, texto) -> {
        Perfil ok;
        try {
          ok = repositorio.autentificar(perfil.getNombre(), texto);
        } catch (BaseDeDatosException e) {
          tarjeta.mostrarError(e.getMessage());
          return;
        }
        if (ok == null) {
          tarjeta.mostrarError("Contraseña incorrecta");
          return;
        }
        Sesion.iniciar(ok);
        Utilidades.cambiarPantalla(tarjeta, "Pokemon Card-Jitsu", "principal.fxml");
      });
    } else {
      tarjeta.setAlCrear((usuario, contrasena) -> {
        Perfil nuevo = new Perfil(usuario, Passwords.generarHash(contrasena));
        try {
          // revisar si existe y guardar es una sola operacion (no hay hueco para duplicados)
          if (!repositorio.agregarUsuario(nuevo)) {
            tarjeta.mostrarError("Usuario en uso");
            return;
          }
        } catch (BaseDeDatosException e) {
          tarjeta.mostrarError("No se ha podido crear el usuario: " + e.getMessage());
          return;
        }
        Utilidades.alertar("Creacion del usuario", "El usuario " + usuario + " se ha creado con exito",
            Alert.AlertType.CONFIRMATION);
        Sesion.iniciar(nuevo);
        Utilidades.cambiarPantalla(tarjeta, "Pokemon Card-Jitsu", "principal.fxml");
      });
    }

    return tarjeta;
  }

}