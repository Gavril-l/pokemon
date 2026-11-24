package com.uabc.poo2.controlador;

import com.uabc.poo2.audio.Musica;
import com.uabc.poo2.audio.Musica.Pista;
import com.uabc.poo2.modelo.HistorialLucha;
import com.uabc.poo2.modelo.HistorialLucha.RegistroPelea;
import com.uabc.poo2.modelo.Perfil;
import com.uabc.poo2.sesion.Sesion;
import com.uabc.poo2.util.Utilidades;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class EstadisticasController implements Initializable {

  @FXML
  private Label lblTitulo;

  @FXML
  private Label lblVictorias;

  @FXML
  private Label lblDerrotas;

  @FXML
  private Label lblPorcentaje;

  @FXML
  private TableView<RegistroPelea> tvHistorial;

  @FXML
  private TableColumn<RegistroPelea, String> colRival;

  @FXML
  private TableColumn<RegistroPelea, String> colResultado;

  @FXML
  private TableColumn<RegistroPelea, String> colFecha;

  @FXML
  private Button btnRegresar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Musica.reproducir(Pista.MENU);

    colRival.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRival()));
    colResultado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getResultado()));
    colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFecha()));

    if (Sesion.hayPerfil()) {
      Perfil perfil = Sesion.getPerfil();
      HistorialLucha historial = perfil.getHistorialLucha();

      lblTitulo.setText("Estadísticas de " + perfil.getNombre());
      lblVictorias.setText(String.valueOf(historial.getVictorias()));
      lblDerrotas.setText(String.valueOf(historial.getDerrotas()));
      lblPorcentaje.setText(String.format("%.1f%%", historial.getPorcentajeVictorias()));
      tvHistorial.setItems(FXCollections.observableArrayList(historial.getRegistros()));
    } else {
      lblTitulo.setText("Estadísticas");
      lblVictorias.setText("0");
      lblDerrotas.setText("0");
      lblPorcentaje.setText("0.0%");
    }
  }

  @FXML
  void onRegresarClick(ActionEvent event) {
    Utilidades.cambiarPantalla(event, "Pokemon Card-Jitsu", "principal.fxml");
  }
}