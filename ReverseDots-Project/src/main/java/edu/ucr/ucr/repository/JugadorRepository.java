package edu.ucr.ucr.repository;

import edu.ucr.ucr.model.Jugador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorRepository implements IJugadorRepository {


    private List<Jugador> listajugadores = new ArrayList<>();
    private File storageFile = new File("jugadores.dat"); // guardamos en este archivo por defecto

    @Override
    public void guardar(Jugador jugador) {

        Jugador encontrado = obtenerPorNombre(jugador.getNombre());

        if (encontrado == null) {
            listajugadores.add(jugador);
        }
        persistirAFichero();
    }

    @Override
    public Jugador obtenerPorNombre(String nombre) {
        // buscaremos si algun nombre coincide
        for (Jugador j : listajugadores) {
            if (j.getNombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

    @Override
    public List<Jugador> obtenerTodosjugadores() {
        // Intentamos cargar desde disco la primera vez si la lista está vacía
        if (listajugadores.isEmpty()) {
            cargarDesdeFichero();
        }
        return listajugadores;
    }

    // Métodos helper para persistencia simple usando serialización
    private void persistirAFichero() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            out.writeObject(listajugadores);
        } catch (IOException e) {
            System.err.println("Error guardando jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDesdeFichero() {
        if (!storageFile.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(storageFile))) {
            Object obj = in.readObject();
            if (obj instanceof List) {
                listajugadores = (List<Jugador>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}