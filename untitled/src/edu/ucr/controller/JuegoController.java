package edu.ucr.controller;

import edu.ucr.model.Ficha;
import edu.ucr.model.Jugador;
import edu.ucr.model.Partida;
import edu.ucr.model.Tablero;
import edu.ucr.repositoy.IJugadorRepository;
import edu.ucr.repositoy.IPartidaRepository;

import javax.swing.*;

public class JuegoController {
    private Tablero tablero;
    private Jugador jugadorBlanco;
    private Jugador jugadorNegro;
    private Ficha turno;
    private IJugadorRepository jugadorRepository;
    private IPartidaRepository iPartidaRepository;

    public JuegoController(IJugadorRepository jRepository, IPartidaRepository pRepository) {
        this.jugadorRepository = jRepository;
        this.iPartidaRepository = pRepository;
        this.turno = Ficha.NEGRA; //siempre empieza el negro
    }

    }

    //logica para iniciar el juego nuevo
    public void iniciarNuevaPartida(int tamaño, String nombreNegro, String nombreBlanco){

        this.tablero = new Tablero(tamaño);

        // obtener o creamos los jugadores en el repositorio
        this.jugadorNegro = obtenerOCrearJudador(nombreNegro);
        this.jugadorBlanco = obtenerOCrearJudador (nombreBlanco);

        this.turno = Ficha.NEGRA;
        System.out.println("Partida iniciada: " + nombreNegro + " VS " + nombreBlanco);

    }

    private Jugador obtenerOCrearJudador(String nombre){
        Jugador jugador = jugadorRepository.obtenerPorNombre(nombre);
        if (jugador == null){
            jugador = new Jugador(nombre);
            jugadorRepository.guardar(jugador);
        }
        return jugador;
    }

    //Validar y realizar un movimiento
    public boolean ejecutarMovimiento (int fila, int columna){
        //celda vacia hace el movimiento
        if (tablero.getFicha(columna, fila) != Ficha.VACIA){
            return false;
        }
        //Aquí irá la lógica de "encerrar fichas"
        // Por ahora, solo ponemos la ficha para probar
        if (esMovimientoValido(fila,columna)){
            tablero.setFicha(fila,columna,turno);
            cambiarTurno();
            return true;
        }
        return false;
    }

    private boolean esMovimientoValido(int fila, int columna){

        // En la siguiente fase probamos todas las direcciones
        // para ver si encierra fichas del jugador contrario.

        return true;
    }

    private void cambiarTurno(){
        //Ese signo de ? junto con los : se llama Operador Ternario.
        // Es básicamente una forma "elegante" y
        // ultra rápida de escribir un en una sola línea.if-else
        this.turno = (turno == Ficha.NEGRA) ? Ficha.BLANCA : Ficha.NEGRA;
    }

    public void guardarProgreso(String ruta){
        Partida partidaActual = new Partida(tablero, jugadorNegro, jugadorBlanco, turno);
        iPartidaRepository.guardarPartida(partidaActual, ruta);
    }
}
