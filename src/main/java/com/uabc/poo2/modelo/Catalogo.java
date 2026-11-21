package com.uabc.poo2.modelo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Catalogo
 */
public class Catalogo {

  // Cambia ".png" a ".jpg"
  private static final String EXT = ".png";
  private static final Map<String, Pokemon> POKEMONES = new LinkedHashMap<>();

  static {
    // Bicho
    agregar(new Pokemon("010", "Caterpie", Tipo.BICHO, 2, "verde", "Caterpie" + EXT));
    agregar(new Pokemon("011", "Metapod", Tipo.BICHO, 4, "verde", "Metapod" + EXT));
    agregar(new Pokemon("012", "Butterfree", Tipo.BICHO, 7, "morado", "Butterfree" + EXT));
    agregar(new Pokemon("013", "Weedle", Tipo.BICHO, 2, "naranja", "Weedle" + EXT));
    agregar(new Pokemon("014", "Kakuna", Tipo.BICHO, 4, "amarillo", "Kakuna" + EXT));
    agregar(new Pokemon("015", "Beedrill", Tipo.BICHO, 7, "amarillo", "Beedrill" + EXT));
    agregar(new Pokemon("046", "Paras", Tipo.PLANTA, 3, "naranja", "Paras" + EXT));
    agregar(new Pokemon("047", "Parasect", Tipo.PLANTA, 6, "rojo", "Parasect" + EXT));
    agregar(new Pokemon("048", "Venonat", Tipo.VENENO, 4, "morado", "Venonat" + EXT));
    agregar(new Pokemon("049", "Venomoth", Tipo.VENENO, 7, "morado", "Venomoth" + EXT));

    // Planta
    agregar(new Pokemon("001", "Bulbasaur", Tipo.PLANTA, 3, "verde", "Bulbasaur" + EXT));
    agregar(new Pokemon("002", "Ivysaur", Tipo.PLANTA, 6, "verde", "Ivysaur" + EXT));
    agregar(new Pokemon("003", "Venusaur", Tipo.PLANTA, 9, "verde", "Venusaur" + EXT));
    agregar(new Pokemon("043", "Oddish", Tipo.PLANTA, 3, "azul", "Oddish" + EXT));
    agregar(new Pokemon("044", "Gloom", Tipo.PLANTA, 6, "azul", "Gloom" + EXT));
    agregar(new Pokemon("045", "Vileplume", Tipo.PLANTA, 9, "rojo", "Vileplume" + EXT));

    // Fuego
    agregar(new Pokemon("004", "Charmander", Tipo.FUEGO, 3, "rojo", "Charmander" + EXT));
    agregar(new Pokemon("005", "Charmeleon", Tipo.FUEGO, 6, "rojo", "Charmeleon" + EXT));
    agregar(new Pokemon("006", "Charizard", Tipo.FUEGO, 10, "rojo", "Charizard" + EXT));
    agregar(new Pokemon("037", "Vulpix", Tipo.FUEGO, 4, "cafe", "Vulpix" + EXT));
    agregar(new Pokemon("038", "Ninetales", Tipo.FUEGO, 8, "amarillo", "Ninetales" + EXT));

    // Agua
    agregar(new Pokemon("007", "Squirtle", Tipo.AGUA, 3, "azul", "Squirtle" + EXT));
    agregar(new Pokemon("008", "Wartortle", Tipo.AGUA, 6, "azul", "Wartortle" + EXT));
    agregar(new Pokemon("009", "Blastoise", Tipo.AGUA, 10, "azul", "Blastoise" + EXT));

    // Electrico
    agregar(new Pokemon("025", "Pikachu", Tipo.ELECTRICO, 5, "amarillo", "Pikachu" + EXT));
    agregar(new Pokemon("026", "Raichu", Tipo.ELECTRICO, 8, "naranja", "Raichu" + EXT));

    // Veneno
    agregar(new Pokemon("023", "Ekans", Tipo.VENENO, 3, "morado", "Ekans" + EXT));
    agregar(new Pokemon("024", "Arbok", Tipo.VENENO, 6, "morado", "Arbok" + EXT));
    agregar(new Pokemon("029", "NidoranH", Tipo.VENENO, 3, "azul", "NidoranH" + EXT));
    agregar(new Pokemon("030", "Nidorina", Tipo.VENENO, 6, "azul", "Nidorina" + EXT));
    agregar(new Pokemon("031", "Nidoqueen", Tipo.VENENO, 9, "azul", "Nidoqueen" + EXT));
    agregar(new Pokemon("032", "NidoranM", Tipo.VENENO, 3, "morado", "NidoranM" + EXT));
    agregar(new Pokemon("033", "Nidorino", Tipo.VENENO, 6, "morado", "Nidorino" + EXT));
    agregar(new Pokemon("034", "Nidoking", Tipo.VENENO, 9, "morado", "Nidoking" + EXT));
    agregar(new Pokemon("041", "Zubat", Tipo.VENENO, 3, "azul", "Zubat" + EXT));
    agregar(new Pokemon("042", "Golbat", Tipo.VENENO, 6, "azul", "Golbat" + EXT));

    // Tierra
    agregar(new Pokemon("027", "Sandshrew", Tipo.TIERRA, 4, "amarillo", "Sandshrew" + EXT));
    agregar(new Pokemon("028", "Sandslash", Tipo.TIERRA, 7, "amarillo", "Sandslash" + EXT));
    agregar(new Pokemon("050", "Diglett", Tipo.TIERRA, 3, "cafe", "Diglett" + EXT));

    // Normal
    agregar(new Pokemon("016", "Pidgey", Tipo.NORMAL, 2, "cafe", "Pidgey" + EXT));
    agregar(new Pokemon("017", "Pidgeotto", Tipo.NORMAL, 5, "cafe", "Pidgeotto" + EXT));
    agregar(new Pokemon("018", "Pidgeot", Tipo.NORMAL, 8, "cafe", "Pidgeot" + EXT));
    agregar(new Pokemon("019", "Rattata", Tipo.NORMAL, 2, "morado", "Rattata" + EXT));
    agregar(new Pokemon("020", "Raticate", Tipo.NORMAL, 5, "cafe", "Raticate" + EXT));
    agregar(new Pokemon("021", "Spearow", Tipo.NORMAL, 3, "cafe", "Spearow" + EXT));
    agregar(new Pokemon("022", "Fearow", Tipo.NORMAL, 6, "cafe", "Fearow" + EXT));
    agregar(new Pokemon("035", "Clefairy", Tipo.NORMAL, 4, "rosa", "Clefairy" + EXT));
    agregar(new Pokemon("036", "Clefable", Tipo.NORMAL, 7, "rosa", "Clefable" + EXT));
    agregar(new Pokemon("039", "Jigglypuff", Tipo.NORMAL, 4, "rosa", "Jigglypuff" + EXT));
    agregar(new Pokemon("040", "Wigglytuff", Tipo.NORMAL, 7, "rosa", "Wigglytuff" + EXT));
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