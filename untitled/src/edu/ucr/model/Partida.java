package edu.ucr.model;

import java.io.Serializable;

public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private Jugador jugadorNegro;
    private Jugador jugadorBlanco;
    private Ficha turno;//aqui se guarda a quien le toca jugar en ese momento


    public Partida(Jugador j1, Jugador j2, int tamañoTablero){
        this.jugadorNegro = j1;
        this.jugadorBlanco = j2;
        this.tablero = new Tablero(tamañoTablero);

        //la ficha negra siempre empieza
        this.turno = Ficha.NEGRA;
    }

    // Se usa para capturar el estado actual del juego
    public Partida(Tablero tablero, Jugador jugadorNegro, Jugador jugadorBlanco, Ficha turno) {
        this.tablero = tablero;
        this.jugadorNegro = jugadorNegro;
        this.jugadorBlanco = jugadorBlanco;
        this.turno = turno;
    }


    public void cambiarTurno() {
        if (this.turno == Ficha.NEGRA) {
            this.turno = Ficha.BLANCA;
        } else {
            this.turno = Ficha.NEGRA;
        }
    }
    public Tablero getTablero() {return tablero;}
    public Ficha getTurno() {return turno;}
    public Jugador getJugadorNegro(){return jugadorNegro;}
    public Jugador getJugadorBlanco(){return jugadorBlanco;}
}