package edu.ucr.ucr.model;

public enum Ficha {
    // 1. Definimos las constantes y les pasamos el texto entre paréntesis
    NEGRA("NEGRO"),
    BLANCA("BLANCO"),
    VACIA("VACIO");

    // 2. Atributo para guardar ese texto
    private final String color;

    // 3. Constructor del enum (esto es lo que faltaba)
    Ficha(String color) {
        this.color = color;
    }

    // 4. El famoso método para obtener el color
    public String getColor() {
        return this.color;
    }
}
