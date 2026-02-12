package edu.ucr.ucr.repository;

import edu.ucr.model.Partida;

import java.io.*;

/*
 ¿Por qué interfaces?: interfaces porque el requerimiento técnico
 exige que la capa Repository esté desacoplada. Así, si mañana queremos cambiar
 de archivos de texto a una base de datos, el resto del juego no se da cuenta.
*/

public class PartidaRepository implements IPartidaRepository {
    @Override
    public void guardarPartida(Partida partida, String rutaArchivo) {

        // En lenguaje cotidiano: escribimos el objeto Partida en un archivo usando serialización Java.
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            out.writeObject(partida);
            System.out.println("Partida guardada en " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error guardando partida: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public Partida cargarPartida(String rutaArchivo) {
        // En lenguaje cotidiano: leemos el objeto Partida desde el archivo y lo devolvemos
        File f = new File(rutaArchivo);
        if (!f.exists()) {
            System.out.println("Archivo no existe: " + rutaArchivo);
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object obj = in.readObject();
            if (obj instanceof Partida) {
                System.out.println("Partida cargada desde " + rutaArchivo);
                return (Partida) obj;
            } else {
                System.err.println("El archivo no contiene una Partida válida");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando partida: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Si algo falla, devolvemos null
    }
}