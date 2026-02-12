package edu.ucr;

import edu.ucr.controller.JuegoController;
import edu.ucr.repository.IJugadorRepository;
import edu.ucr.repository.IPartidaRepository;
import edu.ucr.repository.JugadorRepository;
import edu.ucr.repository.PartidaRepository;
import edu.ucr.view.VentanaJuego;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializar repositorios (recuerda usar tus implementaciones reales)
        // JuegoController controlador = new JuegoController(new JugadorRepoImpl(), new PartidaRepoImpl());
        IJugadorRepository jugadorRepository = new JugadorRepository();
        IPartidaRepository partidaRepository = new PartidaRepository();

        JuegoController controlador = new JuegoController(jugadorRepository, partidaRepository);

        controlador.iniciarNuevaPartida(8, "Negro", "Blanco");

        // 2. Lanzar la interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            VentanaJuego ventana = new VentanaJuego(controlador);
            ventana.setVisible(true);
        });//Holaaaaaaaaaaaaaaaaaaa
    }
}