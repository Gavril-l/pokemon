package com.uabc.poo2.controlador;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import com.uabc.poo2.util.Passwords;
import com.uabc.poo2.util.Utilidades;
import com.uabc.poo2.sesion.Sesion;

import java.net.URL;
import java.util.ResourceBundle;

import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.persistencia.RepositorioPerfiles;
import com.uabc.poo2.vista.componentes.TarjetaPerfil;

public class InicioController implements Initializable {

  RepositorioPerfiles repositorio = RepositorioPerfiles.getInstancia();

  @FXML
  private VBox vbPrincipal;

  @FXML
  private FlowPane fpPerfiles;

  @FXML
  private ScrollPane spPerfiles;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

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
        if (repositorio == null)
          repositorio = RepositorioPerfiles.getInstancia();

        Perfil ok = repositorio.autentificar(perfil.getNombre(), texto);
        if (ok == null) {
          tarjeta.mostrarError("Contraseña incorrecta");
          return;
        }
        Sesion.iniciar(ok);
        repositorio.cerrar();
        Utilidades.cambiarPantalla(tarjeta, "Pokemon Card-Jitsu", "principal.fxml");
      });
    } else {
      tarjeta.setAlCrear((usuario, contrasena) -> {
        if (repositorio == null)
          repositorio = RepositorioPerfiles.getInstancia();
        if (!repositorio.busqueda(usuario).isEmpty()) {
          tarjeta.mostrarError("Usuario en uso");
          return;
        }

        Perfil nuevo = new Perfil(usuario, Passwords.generarHash(contrasena));
        repositorio.agregarUsuario(nuevo);
        Sesion.iniciar(nuevo);
        repositorio.cerrar();
        Utilidades.cambiarPantalla(tarjeta, "Pokemon Card-Jitsu", "principal.fxml");
      });
    }

    return tarjeta;
  }

}
