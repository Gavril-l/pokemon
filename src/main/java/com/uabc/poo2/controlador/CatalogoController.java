package com.uabc.poo2.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.uabc.poo2.modelo.Pokemon;
import com.uabc.poo2.vista.componentes.TarjetaPokemon;
import com.uabc.poo2.modelo.Catalogo;
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
