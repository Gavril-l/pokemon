package com.uabc.poo2.modelo;

/** Ejemplo de clase del modelo. Reemplazala por la logica de tu proyecto. */
public class Saludo {

    private String nombre;

    public Saludo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return "Hola, " + nombre + "!";
    }
}
