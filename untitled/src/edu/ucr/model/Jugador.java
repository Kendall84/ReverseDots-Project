package edu.ucr.model;

public class Jugador {

    private String nombre;
    private int partidasGanada;
    private int partidasPerdida;

    public  Jugador(String nombre) {
        this.nombre = nombre;
        this.partidasGanada = 0;
        this.partidasPerdida = 0;
    }

    //para cambiar datos del jugador
    public String getNombre() {
        return nombre;
    }

    // si gana se llama para aumentar las partidas ganadas
    public void incrementarGanadas() {
        this.partidasGanada++;
    }
    //si pierde se llama para aumentar partidas perdidas
    public void incrementarPerdidas() {
        this.partidasPerdida++;
    }

}
