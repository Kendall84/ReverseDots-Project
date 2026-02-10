package edu.ucr.repository;

import edu.ucr.model.Partida;

/*
 ¿Por qué interfaces?: interfaces porque el requerimiento técnico
 exige que la capa Repository esté desacoplada. Así, si mañana queremos cambiar
 de archivos de texto a una base de datos, el resto del juego no se da cuenta.
*/

public class PartidaRepository implements IPartidaRepository {
    @Override
    public void guardarPartida(Partida partida, String rutaArchivo) {

        //si el archivo existe, preguntamos si se sobreescribe
        //este metodo recibira la orden de la vista para guardar
        System.out.println("Guardando partida en " + rutaArchivo );

        //TODO Aquí irá el código para convertir el tablero en texto y guardarlo

    }

    @Override
    public Partida cargarPartida(String rutaArchivo) {
        System.out.println("Cargando partida en " + rutaArchivo );
        return null; // Temporalmente devuelve nada hasta que hagamos la lectura
    }
}
