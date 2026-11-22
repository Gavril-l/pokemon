package com.uabc.poo2.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Musica
 */
public class Musica {
  public enum Pista {
    MENU, PARTIDA
  };

  private static MediaPlayer menu;
  private static MediaPlayer partida;
  private static MediaPlayer actual;

  public static void iniciar() {
    Media lobby = new Media(Musica.class.getResource("lobby.wav").toExternalForm());
    menu = new MediaPlayer(lobby);
  };

  public static void reproducir(Pista pista) {
    MediaPlayer nuevo;

    switch (pista) {
      case MENU:
        nuevo = menu;
        nuevo.setStopTime(Duration.seconds(63.5));
        break;
      case PARTIDA:
        nuevo = partida;
        break;
      default:
        nuevo = null;
    }
    ;

    if (nuevo == actual)
      return;

    detener();

    actual = nuevo;

    actual.setCycleCount(MediaPlayer.INDEFINITE);
    actual.setVolume(0.4);
    actual.play();
  }

  public static void detener() {
    if (actual != null)
      actual.stop();
  }

}
