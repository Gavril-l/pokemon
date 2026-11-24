package com.uabc.poo2.controlador;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.modelo.Pokemon;
import com.uabc.poo2.modelo.Tipo;
import com.uabc.poo2.persistencia.RepositorioPerfiles;
import com.uabc.poo2.sesion.Sesion;
import com.uabc.poo2.util.Utilidades;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetalleController implements Initializable {

  private static final String RUTA_IMAGENES = "/com/uabc/poo2/vista/imagenes/";
  private static final Set<String> OCULTOS = Set.of("rutaImagen"); // campos internos que no se muestran

  @FXML
  private Button btnRegresar;

  @FXML
  private ImageView ivPokemon;

  @FXML
  private VBox vbDatos;

  private Pokemon pokemon;

  @FXML
  void onRegresarClick(ActionEvent event) {
    Stage stage = (Stage) btnRegresar.getScene().getWindow();
    stage.close();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ivPokemon.setCursor(Cursor.HAND);
    ivPokemon.setOnMouseClicked(e -> {
      Perfil nuevo = RepositorioPerfiles.getInstancia().actualizarPokemonFav(Sesion.getPerfil().getNombre(),
          pokemon.getId());
      Sesion.iniciar(nuevo);
      Utilidades.alertar("Catalogo", "Asignaste a " + pokemon.getNombre() + " como foto de perfil",
          AlertType.INFORMATION);
    });
  }

  public void setPokemon(Pokemon pokemon) {
    this.pokemon = pokemon;
    cargarImagen(pokemon);

    Class<?> clase = pokemon.getClass();
    agregarFila("Clase", clase.getSimpleName());

    for (Field campo : clase.getDeclaredFields()) {
      if (Modifier.isStatic(campo.getModifiers()) || campo.isSynthetic()) {
        continue; // salta serialVersionUID
      }
      if (OCULTOS.contains(campo.getName())) {
        continue;
      }

      String etiqueta = formatear(campo.getName());
      try {
        campo.setAccessible(true); // los campos son private
        agregarFila(etiqueta, texto(campo.get(pokemon)));
      } catch (IllegalAccessException | RuntimeException e) {
        agregarFila(etiqueta, "(no disponible)");
      }
    }
  }

  private void agregarFila(String etiqueta, String valor) {
    Label lblEtiqueta = new Label(etiqueta + ":");
    lblEtiqueta.setStyle("-fx-font-weight: bold;");
    HBox fila = new HBox(20, lblEtiqueta, new Label(valor));
    vbDatos.getChildren().add(fila);
  }

  private static String texto(Object valor) {
    if (valor == null) {
      return "—";
    }
    if (valor instanceof Tipo tipo) {
      return tipo.getNombre();
    }
    return String.valueOf(valor);
  }

  private static String formatear(String nombreCampo) {
    String separado = nombreCampo.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
    return Character.toUpperCase(separado.charAt(0)) + separado.substring(1);
  }

  private void cargarImagen(Pokemon pokemon) {
    URL url = DetalleController.class.getResource(RUTA_IMAGENES + pokemon.getRutaImagen());
    if (url != null) {
      ivPokemon.setImage(new Image(url.toExternalForm()));
    }
  }
}
