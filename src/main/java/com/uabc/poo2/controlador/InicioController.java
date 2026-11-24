package com.uabc.poo2.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

import com.uabc.poo2.util.Passwords;
import com.uabc.poo2.util.Utilidades;
import com.uabc.poo2.sesion.Sesion;

import java.net.URL;
import java.util.ResourceBundle;

import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.persistencia.RepositorioPerfiles;
import com.uabc.poo2.vista.componentes.TarjetaPerfil;

public class InicioController implements Initializable {

  RepositorioPerfiles repositorio;

  @FXML
  private VBox vbPrincipal;

  @FXML
  private FlowPane fpPerfiles;

  @FXML
  private ScrollPane spPerfiles;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    repositorio = RepositorioPerfiles.getInstancia();
    spPerfiles.setFitToWidth(true);

    for (Perfil p : repositorio.listar()) {
      vbPrincipal.getChildren().add(crearTarjeta(p));
    }

    vbPrincipal.getChildren().add(crearTarjeta(null));
  }

  private TarjetaPerfil crearTarjeta(Perfil existente) {
    TarjetaPerfil tarjeta = new TarjetaPerfil(existente);

    if (existente != null) {
      tarjeta.setAlEntrar((perfil, texto) -> {
        Perfil ok = repositorio.autentificar(perfil.getNombre(), texto); // texto plano: el repositorio ya hashea
        if (ok == null) {
          tarjeta.mostrarError("Contraseña incorrecta");
          return;
        }
        Sesion.iniciar(ok);
        Utilidades.cambiarPantalla(tarjeta, "Pokemon Card-Jitsu", "principal.fxml");
      });
    } else {
      tarjeta.setAlCrear((usuario, contrasena) -> {
        if (!repositorio.busqueda(usuario).isEmpty()) { // la lista vacía es lo normal: nombre libre
          tarjeta.mostrarError("Usuario en uso");
          return;
        }

        Perfil nuevo = new Perfil(usuario, Passwords.generarHash(contrasena)); // se guarda el hash
        repositorio.agregarUsuario(nuevo); // ya muestra la alerta de éxito o de error
        Sesion.iniciar(nuevo);
        Utilidades.cambiarPantalla(tarjeta, "Pokemon Card-Jitsu", "principal.fxml");
      });
    }

    return tarjeta;
  }

}
