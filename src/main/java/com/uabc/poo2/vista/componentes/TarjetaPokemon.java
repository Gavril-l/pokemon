package com.uabc.poo2.vista.componentes;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.uabc.poo2.modelo.Pokemon;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
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
  private static final double PROPORCION = 1.4; // alto = ancho * 1.4

  public static final int RESOLUCION_NORMAL = 300; // catalogo
  public static final int RESOLUCION_ALTA = 560; // partida (cartas grandes, pantallas grandes)
  private static final Map<String, Image> IMAGENES = new HashMap<>();

  private Pokemon pokemon;
  private final int resolucion;
  private final DoubleProperty ancho = new SimpleDoubleProperty(140);
  private final ImageView ivPokemon = new ImageView();
  private final Label lblName = new Label();
  private final Label lblPoder = new Label();
  private VBox vbCapaInferior = new VBox();
  private VBox vbCapaSuperior = new VBox();

  public TarjetaPokemon() {
    this(RESOLUCION_NORMAL);
  }

  public TarjetaPokemon(int resolucion) {
    this.resolucion = resolucion;

    ivPokemon.fitWidthProperty().bind(ancho);
    ivPokemon.fitHeightProperty().bind(ancho.multiply(PROPORCION));
    ivPokemon.setPreserveRatio(true);
    ivPokemon.setSmooth(true);
    vbCapaInferior.getChildren().addAll(ivPokemon, lblName);

    // El valor va en la esquina de arriba a la derecha. Su margen y su tamaño
    // son proporcionales al ancho de la carta, para que siempre caiga en el
    // mismo lugar de la imagen.
    vbCapaSuperior.setAlignment(Pos.TOP_RIGHT);
    vbCapaSuperior.paddingProperty().bind(Bindings.createObjectBinding(
        () -> new Insets(ancho.get() * 0.107, ancho.get() * 0.143, ancho.get() * 0.107, ancho.get() * 0.107),
        ancho));
    lblPoder.styleProperty().bind(
        ancho.multiply(0.09).asString(Locale.US, "-fx-font-weight: bold; -fx-font-size: %.1fpx;"));
    vbCapaSuperior.getChildren().add(lblPoder);

    getChildren().addAll(vbCapaInferior, vbCapaSuperior);

    setAlignment(Pos.CENTER);
  }

  public TarjetaPokemon(Pokemon pokemon) {
    this(pokemon, RESOLUCION_NORMAL);
  }

  public TarjetaPokemon(Pokemon pokemon, int resolucion) {
    this(resolucion);
    setPokemon(pokemon);
  }

  public void setAncho(double valor) {
    ancho.unbind();
    ancho.set(valor);
  }

  public void ligarAncho(ObservableValue<? extends Number> valor) {
    ancho.bind(valor);
  }

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
      lblPoder.setText("");
      ivPokemon.setImage(null);
      return;
    }

    lblPoder.setText(Integer.toString(pokemon.getValor()));
    lblName.setText(pokemon.getNombre());
    ivPokemon.setImage(cargarImagen(pokemon.getRutaImagen()));
  }

  // devuelve null en vez de tronar si la imagen no existe en resources
  private Image cargarImagen(String archivo) {
    if (archivo == null) {
      return null;
    }
    String clave = archivo + "@" + resolucion;
    Image imagen = IMAGENES.get(clave);
    if (imagen == null) {
      URL recurso = TarjetaPokemon.class.getResource(RUTA_IMAGENES + archivo);
      if (recurso == null) {
        return null;
      }
      // preserveRatio y smooth en true reduce con calidad y sin deformar
      imagen = new Image(recurso.toExternalForm(), resolucion, resolucion * PROPORCION, true, true);
      IMAGENES.put(clave, imagen);
    }
    return imagen;
  }

}