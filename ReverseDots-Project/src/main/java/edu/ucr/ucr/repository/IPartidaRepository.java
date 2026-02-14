package edu.ucr.ucr.repository;


import edu.ucr.ucr.model.Partida;

public interface IPartidaRepository {

    // contrato de guardar todo el estado: tablero, fichas y jugadores

    void guardarPartida(Partida partida, String rutaArchivo);

    // contrato de cargar el juego tal y como estaba
    Partida cargarPartida(String rutaArchivo);
}
