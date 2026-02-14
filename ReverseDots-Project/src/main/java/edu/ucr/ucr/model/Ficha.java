package edu.ucr.ucr.model;

public enum Ficha {
    NEGRA("NEGRO"),
    BLANCA("BLANCO"),
    VACIA("VACIO");

    private final String color;

    Ficha(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}
