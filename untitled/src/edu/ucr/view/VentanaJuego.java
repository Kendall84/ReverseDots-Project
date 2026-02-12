package edu.ucr.view;

import edu.ucr.controller.JuegoController;
import javax.swing.*;
import java.awt.*;


public class VentanaJuego extends JFrame {
    private JuegoController controlador;
    private JButton[][] botones; // Matriz de botones para representar el tablero
    private JLabel estadoLabel;
    private JLabel jugadorNegroLabel;
    private  JLabel jugadorBlancoLabel;
    private  JLabel conteoNegroLabel;
    private  JLabel conteoBlancoLabel;

    public VentanaJuego(JuegoController controlador) {
        this.controlador = controlador;
        this.botones = new JButton[8][8];


        setTitle(" --Reversi - UCR-- ");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(800, 600);

        setLayout(new BorderLayout());

        //menu siempre visible con acciones basicas
        setJMenuBar(crearMenu());

        // Creamos el panel del tablero con GridLayout
        JPanel panelTablero = new JPanel(new GridLayout(8, 8));
        inicializarTableroGrafico(panelTablero);
        add(panelTablero, BorderLayout.CENTER);

        //panel lateral con la infromacion del juego
        add(crearPanelLateral(), BorderLayout.EAST);

        //Barra de estado abajo
        add(crearPanelEstado(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);

        //primera pintada para que todo aparezca
        actualizarVista();
    }

    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Juego");


        JMenuItem nuevo = new JMenuItem("Nuevo partida");
        nuevo.addActionListener(e -> JOptionPane.showMessageDialog(this, "nueva partida"));

        JMenuItem cargar = new JMenuItem("Cargar partida");
        cargar.addActionListener(e -> JOptionPane.showMessageDialog(this, "cargar partida"));

        JMenuItem guardar = new JMenuItem("Guardar partida");
        guardar.addActionListener(e -> JOptionPane.showMessageDialog(this, "guardar partida"));

        JMenuItem jugadores = new JMenuItem("ver jugadores");
        jugadores.addActionListener(e -> JOptionPane.showMessageDialog(this, "estadisticas"));

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,"deseas salir?" , "confirmar", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
        menu.add(nuevo);
        menu.add(cargar);
        menu.add(guardar);
        menu.add(jugadores);
        menu.addSeparator();
        menu.add(salir);
        menu.add(menu);


        return menuBar;
    }

    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Estadisticas"));

        // Por ahora ponemos nombres fijos, luego los sacas del controlador
        jugadorNegroLabel = new JLabel("Jugador Negro: Negro");
        jugadorBlancoLabel = new JLabel("Jugador Blanco: Blanco");
        conteoNegroLabel = new JLabel("Fichas Negro: 0");
        conteoBlancoLabel = new JLabel("Fichas Blanco: 0");

        panel.add(jugadorNegroLabel);
        panel.add(jugadorBlancoLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(conteoNegroLabel);
        panel.add(conteoBlancoLabel);
        return panel;
    }

    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        estadoLabel = new JLabel("Listo");
        panel.add(estadoLabel, BorderLayout.CENTER);
        return panel;
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
        int conteoNegras = 0;
        int conteoBlancas = 0;


        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                // Le pedimos al controlador el color de esa casilla
                String colorFicha = controlador.getColorEnPosicion(f, c);

                if (colorFicha.equals("NEGRO")) {
                    botones[f][c].setBackground(Color.BLACK);
                    // Quitamos el texto para que solo se vea el color
                    botones[f][c].setText("");
                    conteoNegras++;
                } else if (colorFicha.equals("BLANCO")) {
                    botones[f][c].setBackground(Color.WHITE);
                    botones[f][c].setText("");
                    conteoBlancas++;
                } else {
                    // Si está vacío, volvemos al verde original
                    botones[f][c].setBackground(new Color(34, 139, 34));
                    botones[f][c].setText("");
                }
            }
        }
        // Opcional: Mostrar de quién es el turno en la consola por ahora
        conteoNegroLabel.setText("Fichas Negro: " + conteoNegras);
        conteoBlancoLabel.setText("Fichas Blanco: " + conteoBlancas);
        estadoLabel.setText("Turno actual: " + controlador.getTurnoActual());
    }
}