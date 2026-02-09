package edu.ucr.repositoy;

import edu.ucr.model.Jugador;

import java.util.ArrayList;
import java.util.List;

public class JugadorRepository implements IJugadorRepository {


    private List<Jugador> listajugadores = new ArrayList<>();

    @Override
    public void guardar(Jugador jugador) {

        Jugador encontrado = obtenerPorNombre(jugador.getNombre());

        if (encontrado == null) {
            listajugadores.add(jugador);
        }
        //TODO Nota: Aquí se agregará el código para escribir en el archivo físico
    }

    @Override
    public Jugador obtenerPorNombre(String nombre) {
        //buscamos si algun nombre coicide
        for (Jugador j : listajugadores) {
            if (j.getNombre().equals(nombre)) {
                return j;
            }
        }
            return null;
    }

    @Override
    public List<Jugador> obtenerTodosjugadores() {
        return listajugadores;
    }
}
