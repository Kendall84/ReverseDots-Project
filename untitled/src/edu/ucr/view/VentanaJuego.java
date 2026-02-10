package edu.ucr.view;

import edu.ucr.controller.JuegoController;
import javax.swing.*;
import java.awt.*;


public class VentanaJuego extends JFrame {
    private JuegoController controlador;
    private JButton[][] botones; // Matriz de botones para representar el tablero

    public VentanaJuego(JuegoController controlador) {
        this.controlador = controlador;
        this.botones = new JButton[8][8];


        setTitle(" --Reversi - UCR-- ");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(600, 600);

        setLayout(new BorderLayout());

        // Creamos el panel del tablero con GridLayout
        JPanel panelTablero = new JPanel(new GridLayout(8, 8));
        inicializarTableroGrafico(panelTablero);

        add(panelTablero, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void inicializarTableroGrafico(JPanel panel) {
        for (int fila = 0; fila < 8; fila++) {

            for (int columna = 0; columna < 8; columna++) {


                JButton boton = new JButton();

                boton.setBackground(new Color(34, 139, 34)); // Color verde tipo tapete

                boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                final int f = fila;
                final int c = columna;

                // Evento al hacer clic
                boton.addActionListener(e -> {
                    boolean exito = controlador.ejecutarMovimiento(f, c);
                    if (exito) {
                        actualizarVista();
                    } else {
                        JOptionPane.showMessageDialog(this, "Movimiento inválido");
                    }
                });

                botones[fila][columna] = boton;
                panel.add(boton);
            }
        }
    }

    public void actualizarVista() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                // Le pedimos al controlador el color de esa casilla
                String colorFicha = controlador.getColorEnPosicion(f, c);

                if (colorFicha.equals("NEGRO")) {
                    botones[f][c].setBackground(Color.BLACK);
                    // Quitamos el texto para que solo se vea el color
                    botones[f][c].setText("");
                } else if (colorFicha.equals("BLANCO")) {
                    botones[f][c].setBackground(Color.WHITE);
                    botones[f][c].setText("");
                } else {
                    // Si está vacío, volvemos al verde original
                    botones[f][c].setBackground(new Color(34, 139, 34));
                    botones[f][c].setText("");
                }
            }
        }
        // Opcional: Mostrar de quién es el turno en la consola por ahora
        System.out.println("Turno actual: " + controlador.getTurnoActual());
    }
}