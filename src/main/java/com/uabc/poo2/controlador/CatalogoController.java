package com.uabc.poo2.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.uabc.poo2.modelo.Pokemon;
import com.uabc.poo2.sesion.Sesion;
import com.uabc.poo2.persistencia.RepositorioPerfiles;
import com.uabc.poo2.vista.componentes.TarjetaPokemon;
import com.uabc.poo2.modelo.Catalogo;
import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.modelo.Tipo;
import com.uabc.poo2.util.Utilidades;

public class CatalogoController implements Initializable {

  private final Map<String, Tipo> tiposPorNombre = new HashMap<>();

  @FXML
  private Button btnRegresar;

  @FXML
  private ComboBox<String> cbTipos;

  @FXML
  private ScrollPane spCartas;

  @FXML
  private FlowPane fpCartas;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    cbTipos.getItems().add("Todos");
    for (Tipo tipo : Tipo.values()) {
      cbTipos.getItems().add(tipo.getNombre());
      tiposPorNombre.put(tipo.getNombre(), tipo);
    }

    cbTipos.setPromptText("Todos");

    spCartas.setFitToWidth(true);

    for (Pokemon pokemon : Catalogo.todos()) {
      TarjetaPokemon tarjeta = new TarjetaPokemon(pokemon);
      tarjeta.setCursor(Cursor.HAND);
      tarjeta.setOnMouseClicked(e -> {
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uabc/poo2/vista/detalles.fxml"));
          Scene scene = new Scene(loader.load());
          DetalleController controlador = loader.getController();
          controlador.setPokemon(tarjeta.getPokemon());
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.show();
          // Perfil nuevo =
          // RepositorioPerfiles.getInstancia().actualizarPokemonFav(Sesion.getPerfil().getNombre(),
          // tarjeta.getPokemon().getId());
          // Sesion.iniciar(nuevo);
        } catch (IOException ex) {
        }
      });
      fpCartas.getChildren().add(tarjeta);
    }

  }

  @FXML
  void cambiarTiposVisibles(ActionEvent event) {
    String seleccion = cbTipos.getValue();
    Tipo tipo = tiposPorNombre.get(seleccion);

    fpCartas.getChildren().clear();

    if (tipo == null) {
      for (Pokemon pokemon : Catalogo.todos()) {
        TarjetaPokemon tarjeta = new TarjetaPokemon(pokemon);
        fpCartas.getChildren().add(tarjeta);
      }
    } else {
      for (Pokemon pokemon : Catalogo.todos()) {

        if (pokemon.getTipo() != tipo)
          continue;

        TarjetaPokemon tarjeta = new TarjetaPokemon(pokemon);
        fpCartas.getChildren().add(tarjeta);
      }
    }
  }

  @FXML
  void onRegresarClick(ActionEvent event) {
    Utilidades.cambiarPantalla(event, "", "principal.fxml");
  }

}
