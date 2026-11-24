package com.uabc.poo2.vista.componentes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import com.uabc.poo2.modelo.Catalogo;
import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.util.Utilidades;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TarjetaPerfil extends HBox implements Initializable {

  @FXML
  private ImageView ivPokemon;

  @FXML
  private Button btnCrearPerfil;

  @FXML
  private Button btnCancelar;

  @FXML
  private Label lblDerrotas;

  @FXML
  private Label lblNombre;

  @FXML
  private Label lblNombre2;

  @FXML
  private Label lblVictorias;

  @FXML
  private PasswordField pfContraseña;

  @FXML
  private PasswordField pfNuevaContraseña;

  @FXML
  private StackPane spContenedor;

  @FXML
  private TextField txtIngresarNombre;

  @FXML
  private VBox vistaContraseña;

  @FXML
  private VBox vistaNormal;
  @FXML
  private VBox vistaRegistro;

  public enum Modo {
    NORMAL,
    CONTRASENA,
    REGISTRO
  }

  private static final String RUTA_FXML = "/com/uabc/poo2/vista/componentes/tarjetaPerfil.fxml";
  private final String RUTA_IMAGENES = "/com/uabc/poo2/vista/imagenes/";
  private Perfil perfil;
  private Modo modoActual;
  private BiConsumer<Perfil, String> alEntrar;

  private BiConsumer<String, String> alCrear;

  public TarjetaPerfil() {
    this(null);
  }

  public TarjetaPerfil(Perfil perfil) {
    this.perfil = perfil;
    FXMLLoader loader = new FXMLLoader(TarjetaPerfil.class.getResource(RUTA_FXML));
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      System.err.println("Error cargando tarjeta");
      e.printStackTrace();
    }
    setModo(perfil == null ? Modo.REGISTRO : Modo.NORMAL);
  }

  public void setAlEntrar(BiConsumer<Perfil, String> alEntrar) {
    this.alEntrar = alEntrar;
  }

  public void setAlCrear(BiConsumer<String, String> alCrear) {
    this.alCrear = alCrear;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setStyle("-fx-background-color: #ffffff");
    setModo(Modo.NORMAL);
    setOnMouseClicked(e -> {
      if (modoActual == Modo.NORMAL)
        setModo(Modo.CONTRASENA);
    });
    String ruta;
    URL recurso;
    if (perfil != null) {
      lblNombre.setText(perfil.getNombre());
      lblNombre2.setText(perfil.getNombre());
      lblVictorias.setText("Victorias: " + perfil.getVictorias());
      lblDerrotas.setText("Derrotas: " + perfil.getDerrotas());
      setCursor(Cursor.HAND);
      ruta = RUTA_IMAGENES + Catalogo.buscar(perfil.getIdPokemonFavorito()).getRutaImagen();
      recurso = getClass().getResource(ruta);
    } else {
      ruta = RUTA_IMAGENES + Catalogo.buscar("008").getRutaImagen();
      recurso = getClass().getResource(ruta);
    }

    ivPokemon.setImage(new Image(recurso.toExternalForm()));

  }

  public void mostrarError(String texto) {
    Utilidades.alertar("Perfil", texto, AlertType.WARNING);
    if (modoActual == Modo.CONTRASENA) {
      pfContraseña.clear();
      pfContraseña.requestFocus();
    }
  }

  @FXML
  void onCancelarAction(ActionEvent event) {
    if (perfil == null)
      return;
    pfContraseña.clear();
    setModo(Modo.NORMAL);
  }

  @FXML
  void onCrearPerfilAction(ActionEvent event) {
    String nombre = txtIngresarNombre.getText().trim();
    String contrasena = pfNuevaContraseña.getText();
    if (nombre.isEmpty()) {
      mostrarError("Escribe un nombre de usuario");
      return;
    }
    if (nombre.contains(";")) {
      mostrarError("El nombre no puede llevar ';'");
      return;
    }
    if (contrasena.length() < 4) {
      mostrarError("La contraseña necesita 4 caracteres o más");
      return;
    }

    if (alCrear != null)
      alCrear.accept(nombre, contrasena);

  }

  @FXML
  void onEntrarAction(ActionEvent event) {
    if (alEntrar != null) {
      alEntrar.accept(perfil, pfContraseña.getText());
    }
  }

  private void setModo(Modo modo) {
    modoActual = modo;
    switch (modoActual) {
      case NORMAL:
        vistaNormal.setVisible(true);
        vistaNormal.setManaged(true);
        vistaRegistro.setVisible(false);
        vistaRegistro.setManaged(false);
        vistaContraseña.setVisible(false);
        vistaContraseña.setManaged(false);
        break;
      case CONTRASENA:
        vistaContraseña.setVisible(true);
        vistaContraseña.setManaged(true);
        vistaRegistro.setVisible(false);
        vistaRegistro.setManaged(false);
        vistaNormal.setVisible(false);
        vistaNormal.setManaged(false);
        break;
      case REGISTRO:
        vistaRegistro.setVisible(true);
        vistaRegistro.setManaged(true);
        vistaContraseña.setVisible(false);
        vistaContraseña.setManaged(false);
        vistaNormal.setVisible(false);
        vistaNormal.setManaged(false);
        break;
    }
  }

}