package com.uabc.poo2.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MotorJuego {

  public static final int TAMANO_MANO = 5;
  public static final int RONDAS_PARA_GANAR = 5;
  public static final int VICTORIAS_TIPO_PARA_GANAR = 3;

  public enum ResultadoRonda {
    GANA_JUGADOR_1,
    GANA_JUGADOR_2,
    EMPATE
  }

  private final Random random = new Random();

  public List<Pokemon> crearManoInicial() {
    List<Pokemon> disponibles = new ArrayList<>(Catalogo.todos());
    Collections.shuffle(disponibles, random);
    List<Pokemon> mano = new ArrayList<>();
    for (int i = 0; i < TAMANO_MANO && i < disponibles.size(); i++) {
      mano.add(disponibles.get(i));
    }
    return mano;
  }

    public void usarYReponerCarta(List<Pokemon> mano, Pokemon jugada) {
    if (mano == null || jugada == null) {
      return;
    }
    int indice = -1;
    for (int i = 0; i < mano.size(); i++) {
      Pokemon actual = mano.get(i);
      if (actual != null && jugada.getId() != null && jugada.getId().equals(actual.getId())) {
        indice = i;
        break;
      }
    }

    List<Pokemon> candidatos = new ArrayList<>(Catalogo.todos());
    candidatos.removeIf(p -> mano.stream().anyMatch(m -> m.getId() != null && m.getId().equals(p.getId())));
    if (candidatos.isEmpty()) {
      candidatos = new ArrayList<>(Catalogo.todos());
    }

    if (indice >= 0 && !candidatos.isEmpty()) {
      mano.set(indice, candidatos.get(random.nextInt(candidatos.size())));
    }
  }

  public Pokemon elegirCartaAleatoria(List<Pokemon> mano) {
    if (mano == null || mano.isEmpty()) {
      List<Pokemon> todos = Catalogo.todos();
      return todos.get(random.nextInt(todos.size()));
    }
    return mano.get(random.nextInt(mano.size()));
  }

  /**
   * Evalua el enfrentamiento entre carta1 (jugador 1) y carta2 (jugador 2).
   * Revisa relaciones de tipo (INMUNE, RESISTE, DEBIL, NEUTRAL) y desempata por valor.
   */
  public ResultadoRonda resolverRonda(Pokemon carta1, Pokemon carta2) {
    if (carta1 == null && carta2 == null) return ResultadoRonda.EMPATE;
    if (carta1 == null) return ResultadoRonda.GANA_JUGADOR_2;
    if (carta2 == null) return ResultadoRonda.GANA_JUGADOR_1;

    Efectividad rel1 = carta1.relacionCon(carta2.getTipo());
    Efectividad rel2 = carta2.relacionCon(carta1.getTipo());

    // si carta2 es inmune al tipo de carta1 y carta1 no es inmune a carta2
    if (rel2 == Efectividad.INMUNE && rel1 != Efectividad.INMUNE) {
      return ResultadoRonda.GANA_JUGADOR_2;
    }
    if (rel1 == Efectividad.INMUNE && rel2 != Efectividad.INMUNE) {
      return ResultadoRonda.GANA_JUGADOR_1;
    }

    int puntajeTipo1 = puntajeEfectividad(rel1);
    int puntajeTipo2 = puntajeEfectividad(rel2);

    if (puntajeTipo1 > puntajeTipo2) {
      return ResultadoRonda.GANA_JUGADOR_1;
    }
    if (puntajeTipo2 > puntajeTipo1) {
      return ResultadoRonda.GANA_JUGADOR_2;
    }

    // Empate en relacion de tipos: gana el de mayor valor base
    if (carta1.getValor() > carta2.getValor()) {
      return ResultadoRonda.GANA_JUGADOR_1;
    }
    if (carta2.getValor() > carta1.getValor()) {
      return ResultadoRonda.GANA_JUGADOR_2;
    }

    return ResultadoRonda.EMPATE;
  }

  private int puntajeEfectividad(Efectividad ef) {
    if (ef == null) return 0;
    return switch (ef) {
      case RESISTE -> 1;
      case NEUTRAL -> 0;
      case DEBIL -> -1;
      case INMUNE -> 2;
    };
  }

  public static class EstadoJugador {
    private int rondasGanadas = 0;
    private final Map<Tipo, Integer> victoriasPorTipo = new EnumMap<>(Tipo.class);

    public void registrarVictoria(Tipo tipoGanador) {
      rondasGanadas++;
      if (tipoGanador != null) {
        victoriasPorTipo.merge(tipoGanador, 1, Integer::sum);
      }
    }

    public int getRondasGanadas() {
      return rondasGanadas;
    }

    public int getMaxVictoriasMismoTipo() {
      return victoriasPorTipo.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public boolean haGanadoPartida() {
      return rondasGanadas >= RONDAS_PARA_GANAR || getMaxVictoriasMismoTipo() >= VICTORIAS_TIPO_PARA_GANAR;
    }
  }
}