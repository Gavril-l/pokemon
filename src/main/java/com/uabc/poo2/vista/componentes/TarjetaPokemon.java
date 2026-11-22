package com.uabc.poo2.vista.componentes;

import java.net.URL;

import com.uabc.poo2.modelo.Pokemon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * TarjetaPokemon
 */
public class TarjetaPokemon extends StackPane {

  private static final String RUTA_IMAGENES = "/com/uabc/poo2/vista/imagenes/";
  private Pokemon pokemon;
  private final ImageView ivPokemon = new ImageView();
  private final Label lblName = new Label();
  private final Label lblPoder = new Label();
  private VBox vbCapaInferior = new VBox();
  private VBox vbCapaSuperior = new VBox();

  public TarjetaPokemon() {
    ivPokemon.setFitWidth(140);
    ivPokemon.setPreserveRatio(true);
    vbCapaInferior.getChildren().addAll(ivPokemon, lblName);

    vbCapaSuperior.setAlignment(Pos.TOP_RIGHT);
    // vbCapaSuperior.setPadding(new Insets(15));
    vbCapaSuperior.setPadding(new Insets(15, 20, 15, 15));
    vbCapaSuperior.getChildren().add(lblPoder);

    getChildren().addAll(vbCapaInferior, vbCapaSuperior);

    setAlignment(Pos.CENTER);
  }

  public TarjetaPokemon(Pokemon pokemon) {
    this();
    setPokemon(pokemon);
  }

  public void setTamanoImagen(double ancho, double alto, boolean conservarProporcion) {
    ivPokemon.setFitWidth(ancho);
    if (alto > 0) {
      ivPokemon.setFitHeight(alto);
    }
    ivPokemon.setPreserveRatio(conservarProporcion);
  }

  // Si prefieres que rellene exactamente el alto y ancho sin dejar márgenes
  // aunque cambie la proporción
  // cambia true por false en setTamanoImagen(ancho, alto, false)
  public void setMostrarNombre(boolean mostrar) {
    lblName.setVisible(mostrar);
    lblName.setManaged(mostrar);
  }

  public Pokemon getPokemon() {
    return pokemon;
  }

  public void setPokemon(Pokemon pokemon) {
    this.pokemon = pokemon;
    if (pokemon == null) {
      lblName.setText("Nombre");
      ivPokemon.setImage(null);
      return;
    }

    lblPoder.setText(Integer.toString(pokemon.getValor()));
    lblPoder.setStyle("-fx-font-weight: bold;");

    lblName.setText(pokemon.getNombre());
    String ruta = RUTA_IMAGENES + pokemon.getRutaImagen();
    System.out.println("Buscando: " + ruta);
    URL recurso = getClass().getResource(ruta);
    System.out.println("Resultado: " + recurso);
    if (recurso != null) {
      ivPokemon.setImage(new Image(recurso.toExternalForm()));
    } else {
      ivPokemon.setImage(null);
    }
  }

}
