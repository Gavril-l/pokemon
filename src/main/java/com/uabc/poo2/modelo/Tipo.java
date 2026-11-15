package com.uabc.poo2.modelo;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Tipo
 */
public enum Tipo {
  NORMAL("Normal"),
  FUEGO("Fuego"),
  AGUA("Agua"),
  PLANTA("Planta"),
  ELECTRICO("Electrico"),
  HIELO("Hielo"),
  LUCHA("Lucha"),
  VENENO("Veneno"),
  TIERRA("Tierra"),
  VOLADOR("Volador"),
  PSIQUICO("Psiquico"),
  BICHO("Bicho"),
  ROCA("Roca"),
  FANTASMA("Fantasma"),
  DRAGON("Dragon"),
  SINIESTRO("Siniestro"),
  ACERO("Acero"),
  HADA("Hada");

  private static final Map<Tipo, Set<Tipo>> DEBILIDADES = new EnumMap<>(Tipo.class);
  private static final Map<Tipo, Set<Tipo>> RESISTENCIAS = new EnumMap<>(Tipo.class);
  private static final Map<Tipo, Set<Tipo>> INMUNIDADES = new EnumMap<>(Tipo.class);

  static {
    DEBILIDADES.put(NORMAL, EnumSet.of(LUCHA));
    DEBILIDADES.put(FUEGO, EnumSet.of(AGUA, TIERRA, ROCA));
    DEBILIDADES.put(AGUA, EnumSet.of(PLANTA, ELECTRICO));
    DEBILIDADES.put(PLANTA, EnumSet.of(FUEGO, HIELO, VOLADOR, BICHO, VENENO));
    DEBILIDADES.put(ELECTRICO, EnumSet.of(TIERRA));
    DEBILIDADES.put(HIELO, EnumSet.of(FUEGO, ROCA, LUCHA, ACERO));
    DEBILIDADES.put(LUCHA, EnumSet.of(VOLADOR, PSIQUICO, HADA));
    DEBILIDADES.put(VENENO, EnumSet.of(TIERRA, PSIQUICO));
    DEBILIDADES.put(TIERRA, EnumSet.of(AGUA, PLANTA, HIELO));
    DEBILIDADES.put(VOLADOR, EnumSet.of(HIELO, ELECTRICO, ROCA));
    DEBILIDADES.put(PSIQUICO, EnumSet.of(BICHO, FANTASMA, SINIESTRO));
    DEBILIDADES.put(BICHO, EnumSet.of(FUEGO, VOLADOR, ROCA));
    DEBILIDADES.put(ROCA, EnumSet.of(AGUA, PLANTA, LUCHA, TIERRA, ACERO));
    DEBILIDADES.put(FANTASMA, EnumSet.of(FANTASMA, SINIESTRO));
    DEBILIDADES.put(DRAGON, EnumSet.of(HIELO, HADA, DRAGON));
    DEBILIDADES.put(SINIESTRO, EnumSet.of(LUCHA, BICHO, HADA));
    DEBILIDADES.put(ACERO, EnumSet.of(LUCHA, FUEGO, TIERRA));
    DEBILIDADES.put(HADA, EnumSet.of(VENENO, ACERO));
  }

  static {
    RESISTENCIAS.put(FUEGO, EnumSet.of(FUEGO, PLANTA, HIELO, BICHO, ACERO, HADA));
    RESISTENCIAS.put(AGUA, EnumSet.of(FUEGO, AGUA, HIELO, ACERO));
    RESISTENCIAS.put(PLANTA, EnumSet.of(AGUA, ELECTRICO, PLANTA, TIERRA));
    RESISTENCIAS.put(ELECTRICO, EnumSet.of(ELECTRICO, VOLADOR, ACERO));
    RESISTENCIAS.put(HIELO, EnumSet.of(HIELO));
    RESISTENCIAS.put(LUCHA, EnumSet.of(BICHO, ROCA, SINIESTRO));
    RESISTENCIAS.put(VENENO, EnumSet.of(PLANTA, LUCHA, VENENO, BICHO, HADA));
    RESISTENCIAS.put(TIERRA, EnumSet.of(VENENO, ROCA));
    RESISTENCIAS.put(VOLADOR, EnumSet.of(PLANTA, LUCHA, BICHO));
    RESISTENCIAS.put(PSIQUICO, EnumSet.of(LUCHA, PSIQUICO));
    RESISTENCIAS.put(BICHO, EnumSet.of(PLANTA, LUCHA, TIERRA));
    RESISTENCIAS.put(ROCA, EnumSet.of(NORMAL, FUEGO, VENENO, VOLADOR));
    RESISTENCIAS.put(FANTASMA, EnumSet.of(VENENO, BICHO));
    RESISTENCIAS.put(DRAGON, EnumSet.of(FUEGO, AGUA, ELECTRICO, PLANTA));
    RESISTENCIAS.put(SINIESTRO, EnumSet.of(FANTASMA, SINIESTRO));
    RESISTENCIAS.put(ACERO, EnumSet.of(NORMAL, PLANTA, HIELO, VOLADOR, PSIQUICO, BICHO, ROCA, DRAGON, ACERO, HADA));
    RESISTENCIAS.put(HADA, EnumSet.of(LUCHA, BICHO, SINIESTRO));
  }

  static {
    INMUNIDADES.put(VOLADOR, EnumSet.of(TIERRA));
    INMUNIDADES.put(HADA, EnumSet.of(DRAGON));
    INMUNIDADES.put(NORMAL, EnumSet.of(FANTASMA));
    INMUNIDADES.put(FANTASMA, EnumSet.of(NORMAL, LUCHA));
    INMUNIDADES.put(ACERO, EnumSet.of(VENENO));
    INMUNIDADES.put(SINIESTRO, EnumSet.of(PSIQUICO));
    INMUNIDADES.put(TIERRA, EnumSet.of(ELECTRICO));
  }

  private final String nombre;

  Tipo(String nombre) {
    this.nombre = nombre;
  }

  public Set<Tipo> getDebilidades() {
    return DEBILIDADES.getOrDefault(this, EnumSet.noneOf(Tipo.class));
  }

  public Set<Tipo> getResistencias() {
    return RESISTENCIAS.getOrDefault(this, EnumSet.noneOf(Tipo.class));
  }

  public Set<Tipo> getInmunidades() {
    return INMUNIDADES.getOrDefault(this, EnumSet.noneOf(Tipo.class));
  }

  public String getNombre() {
    return nombre;
  }

  @Override
  public String toString() {
    return nombre;
  }
}
