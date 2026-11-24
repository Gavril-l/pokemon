package com.uabc.poo2.vista;

import com.uabc.poo2.audio.Musica;
import com.uabc.poo2.persistencia.RepositorioPerfiles;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Musica.iniciar();
    Musica.reproducir(Musica.Pista.MENU);
    FXMLLoader loader = new FXMLLoader(App.class.getResource("inicio.fxml"));
    stage.setScene(new Scene(loader.load()));
    stage.setTitle("pokemon");
    stage.setMaximized(true);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void stop() throws Exception {
    Musica.detener();
    RepositorioPerfiles.cerrarSiExiste();
  }
}