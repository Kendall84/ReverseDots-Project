package edu.ucr.ucr.repository;

import edu.ucr.ucr.model.Jugador;

import java.util.List;

public interface IJugadorRepository {

    void guardar(Jugador jugador);

    // Para buscar a un jugador por su nombre único
    Jugador obtenerPorNombre(String nombre);

    // Para sacar la lista de todos y ver quién va ganando
    List<Jugador> obtenerTodosjugadores();


}
