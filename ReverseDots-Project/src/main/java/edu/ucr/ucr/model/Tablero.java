package edu.ucr.ucr.model;

import java.io.Serializable;

public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;

    private Ficha[][] celdas;
    private int tamaño;


    public Tablero(int n) {
        this.tamaño = n;
        this.celdas = new Ficha[n][n];
        prepararTableroNuevo();
    }

    public void prepararTableroNuevo() {
        //dejamos todo el tablero limpio (sin fichas)
        for (int fila = 0; fila < tamaño; fila++) {
            for (int columna = 0; columna < tamaño; columna++) {
                celdas[fila][columna] = Ficha.VACIA;
            }

        }

        // Ahora, ponemos las 4 fichas del centro como dice la regla
        // Calculamos la mitad para saber dónde es el centro
        int mitad = tamaño / 2;

        // Ponemos dos blancas y dos negras cruzadas en los 4 espacios del centro

        celdas[mitad - 1][mitad - 1] = Ficha.BLANCA;
        celdas[mitad][mitad] = Ficha.BLANCA;
        celdas[mitad - 1][mitad] = Ficha.NEGRA;
        celdas[mitad][mitad - 1] = Ficha.NEGRA;

    }
    public boolean esPosicionValida(int fila, int columna) {
        // Revisa que la fila y la columna no se salgan de la matriz
        // El tamaño suele ser 8, así que revisa que esté entre 0 y 7
        return fila >= 0 && fila < tamaño && columna >= 0 && columna < tamaño;
    }
        // Para saber qué hay en una posición específica
        public Ficha getFicha(int fila, int columna) {
            return celdas[fila][columna];
        }
        // Para poner o cambiar una ficha (por ejemplo, cuando se voltea una)
        public void setFicha(int fila, int col, Ficha Ficha) {
            celdas[fila][col] = Ficha;
        }

        public int getTamaño() {
            return tamaño;
        }
    }
