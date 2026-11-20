package com.uabc.poo2.vista.componentes;

import com.uabc.poo2.modelo.Pokemon;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * TarjetaPokemon
 */
public class TarjetaPokemon extends VBox {

  private static final String RUTA_IMAGENES = "/com/uabc/poo2/vista/imagenes/";
  private Pokemon pokemon;
  private final ImageView ivPokemon = new ImageView();
  private final Label lblName = new Label();

  public TarjetaPokemon() {
    ivPokemon.setFitWidth(100);
    ivPokemon.setPreserveRatio(true);

    getChildren().addAll(ivPokemon, lblName);
    setAlignment(Pos.CENTER);
  }

  public TarjetaPokemon(Pokemon pokemon) {
    this();
    setPokemon(pokemon);
    // getStylesheets().add(getClass().getResource("tarjeta-pokemon.css").toExternalForm());
    // getStyleClass().add("tarjeta-pokemon");
  }

  public void setPokemon(Pokemon pokemon) {
    if (pokemon == null) {
      lblName.setText("Nombre");
      ivPokemon.setImage(null);
      return;
    }
    lblName.setText(pokemon.getNombre());
    String ruta = RUTA_IMAGENES + pokemon.getRutaImagen(); // o la ruta del css
    System.out.println("Buscando: " + ruta);
    System.out.println("Resultado: " + getClass().getResource(ruta));
    ivPokemon.setImage(new Image(getClass().getResource(RUTA_IMAGENES + pokemon.getRutaImagen()).toExternalForm()));
  }

}
