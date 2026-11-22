package com.uabc.poo2.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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

    MediaPlayer nuevo = switch (pista) {
      case MENU -> menu;
      case PARTIDA -> partida;
    };

    if (nuevo == actual)
      return;

    if (actual != null)
      actual.pause();

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
