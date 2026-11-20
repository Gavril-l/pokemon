package com.uabc.poo2.modelo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Catalogo
 */
public class Catalogo {

  private static final Map<String, Pokemon> POKEMONES = new LinkedHashMap<String, Pokemon>();

  static {
    agregar(new Pokemon("1", "charmander", Tipo.FUEGO, 1, "rojo", "charmander.jpg"));
  }

  public static Pokemon buscar(String id) {
    return POKEMONES.get(id);
  }

  public static List<Pokemon> todos() {
    return List.copyOf(POKEMONES.values());
  }

  public static List<Pokemon> repartir() {
    // Logica de repartir cartas
    return null;
  }

  private static void agregar(Pokemon p) {
    if (POKEMONES.put(p.getId(), p) != null)
      throw new IllegalStateException("Id repetido: " + p.getId());
  }

}
