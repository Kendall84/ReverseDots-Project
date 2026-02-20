package edu.ucr.ucr.view;

import edu.ucr.ucr.controller.JuegoController;
import edu.ucr.ucr.controller.ResultadoOperacion;

import javax.swing.*;
import java.awt.*;


public class VentanaJuego  extends JFrame {
     private JuegoController controlador;
    // Iconos cacheados para no recrearlos en cada actualización
    private static final Icon ICON_NEGRO = new DiscIcon("NEGRO");
    private static final Icon ICON_BLANCO = new DiscIcon("BLANCO");
     private JButton[][] botones; // Matriz de botones para representar el tablero
     private JLabel estadoLabel;
     private JLabel jugadorNegroLabel;
     private  JLabel jugadorBlancoLabel;
     private  JLabel conteoNegroLabel;
     private  JLabel conteoBlancoLabel;

    private JPanel panelTablero;

    public VentanaJuego(JuegoController controlador) {
        this.controlador = controlador;
        // ahora creamos la matriz de botones según el tamaño del tablero que indique el controlador
        int n = controlador.getTamañoTablero();
        this.botones = new JButton[n][n];

        setTitle(" --Reversi - UCR-- ");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(800, 600);

        setLayout(new BorderLayout());

        //menu siempre visible con acciones basicas
        setJMenuBar(crearMenu());

        // Creamos el panel del tablero con GridLayout
        panelTablero = new JPanel(new GridLayout(n, n));
        inicializarTableroGrafico(panelTablero, n);
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


        JMenuItem nuevo = new JMenuItem("Nueva partida");
        // TODO Cambio: ahora abrimos un diálogo para pedir tamaño y nombres y crear la partida
        nuevo.addActionListener(e -> abrirDialogoNuevaPartida());

        JMenuItem cargar = new JMenuItem("Cargar partida");
        // abrimos un JFileChooser para seleccionar archivo y pedimos al controlador que lo cargue
        cargar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                boolean ok = controlador.cargarPartidaDesdeArchivo(ruta);
                if (ok) {
                    reconstruirTableroUI();
                    actualizarVista();
                    JOptionPane.showMessageDialog(this, "Partida cargada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cargar la partida.");
                }
            }
        });//Nelson

        JMenuItem guardar = new JMenuItem("Guardar partida");
        //abrimos JFileChooser para elegir donde guardar y pedimos al controlador guardar la partida
        guardar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                controlador.guardarProgreso(ruta);
                JOptionPane.showMessageDialog(this, "Partida guardada (si hubo éxito, se indicó en consola).");
            }
        });

        JMenuItem jugadores = new JMenuItem("Ver jugadores");
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
        menuBar.add(menu);


        return menuBar;
    }

    // Nuevo método: abre un diálogo para crear nueva partida
    //le pregunta al usuario el tamaño del tablero y los nombres de los jugadores,
    // valida lo básico y llama al controlador para iniciar la partida.
    private void abrirDialogoNuevaPartida() {
        try {
            String tamañoStr = JOptionPane.showInputDialog(this, "Tamaño del tablero (ej. 8):", "8");
            if (tamañoStr == null) return; // Usuario canceló
            int tamaño = Integer.parseInt(tamañoStr.trim());
            if (tamaño < 4 || tamaño % 2 != 0) {
                JOptionPane.showMessageDialog(this, "El tamaño debe ser un número par mayor o igual a 4.");
                return;
            }

            String nombreNegro = JOptionPane.showInputDialog(this, "Nombre jugador Negro:", "Negro");
            if (nombreNegro == null) return;
            nombreNegro = nombreNegro.trim();
            if (nombreNegro.isEmpty()) nombreNegro = "Negro";

            String nombreBlanco = JOptionPane.showInputDialog(this, "Nombre jugador Blanco:", "Blanco");
            if (nombreBlanco == null) return;
            nombreBlanco = nombreBlanco.trim();
            if (nombreBlanco.isEmpty()) nombreBlanco = "Blanco";

            // Llamamos al controlador para iniciar nueva partida
            controlador.iniciarNuevaPartida(tamaño, nombreNegro, nombreBlanco);

            // Reconstruimos la interfaz del tablero según el nuevo tamaño
            reconstruirTableroUI();

            // Actualizamos la vista completa (nombres y contadores)
            actualizarVista();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tamaño no válido. Debes ingresar un número.");
        } catch (Exception ex) {
            // Mensaje sencillo en lenguaje cotidiano
            JOptionPane.showMessageDialog(this, "Ocurrió un error al crear la partida: " + ex.getMessage());
        }
    }

    // Reconstruye el panel del tablero y la matriz de botones cuando cambia el tamaño
    private void reconstruirTableroUI() {
        int n = controlador.getTamañoTablero();
        // Creamos nuevas estructuras
        this.botones = new JButton[n][n];

        // Quitamos el panel antiguo y creamos uno nuevo
        Container content = getContentPane();
        content.remove(panelTablero);

        panelTablero = new JPanel(new GridLayout(n, n));
        inicializarTableroGrafico(panelTablero, n);
        content.add(panelTablero, BorderLayout.CENTER);

        // Forzamos revalidación y pintado
        content.revalidate();
        content.repaint();
    }

    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Estadisticas"));

        // Ahora usamos los nombres reales del controlador si existen
        String nombreNegro = (controlador.getJugadorNegro() != null) ? controlador.getJugadorNegro().getNombre() : "Negro";
        String nombreBlanco = (controlador.getJugadorBlanco() != null) ? controlador.getJugadorBlanco().getNombre() : "Blanco";
        jugadorNegroLabel = new JLabel("Jugador Negro: " + nombreNegro);
        jugadorBlancoLabel = new JLabel("Jugador Blanco: " + nombreBlanco);
        conteoNegroLabel = new JLabel("Fichas Negro: " + controlador.getConteoNegro());
        conteoBlancoLabel = new JLabel("Fichas Blanco: " + controlador.getConteoBlanco());

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


    // inicializarTableroGrafico ahora recibe 'n' para construir un tablero de tamaño dinámico
    private void inicializarTableroGrafico(JPanel panel, int n) {
        for (int fila = 0; fila < n; fila++) {
            for (int columna = 0; columna < n; columna++) {
                // Creamos un botón que mostrará un icono personalizado con las fichas
                JButton boton = new JButton();

                // Le damos un tamaño preferible para que se vea cuadrado
                boton.setPreferredSize(new Dimension(60, 60));

                // Volvemos a poner el fondo verde del "tapete" en la casilla
                boton.setBackground(new Color(34, 139, 34)); // "tapete" verde
                boton.setOpaque(true);
                boton.setText("");

                final int f = fila;
                final int c = columna;

                // Evento al hacer clic: intentamos ejecutar el movimiento a través del controlador
                boton.addActionListener(e -> {
                    // Ahora el controlador devuelve un ResultadoOperacion para saber el motivo
                    ResultadoOperacion res = controlador.ejecutarMovimiento(f, c);
                    switch (res) {
                        case SUCCESS:
                            actualizarVista();
                            break;
                        case INVALID_POSITION:
                            JOptionPane.showMessageDialog(this, "Posición inválida.");
                            break;
                        case CELL_NOT_EMPTY:
                            JOptionPane.showMessageDialog(this, "La casilla ya tiene una ficha.");
                            break;
                        case INVALID_MOVE:
                            JOptionPane.showMessageDialog(this, "Movimiento inválido: no captura fichas.");
                            break;
                        case ERROR:
                        default:
                            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar mover.");
                            break;
                    }
                });

                botones[fila][columna] = boton;
                panel.add(boton);
            }
        }
    }

    public void actualizarVista() {
        // Ahora que el tamaño del tablero puede variar, pedimos el tamaño real al controlador
        int n = controlador.getTamañoTablero();
        int conteoNegras = 0;
        int conteoBlancas = 0;

        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                // Pedimos al controlador el color de esa casilla
                String colorFicha = controlador.getColorEnPosicion(f, c);

                // Si hay ficha, ponemos el icono redondo correspondiente;
                // si está vacío, dejamos sólo el fondo verde (sin icono)
                if ("NEGRO".equals(colorFicha)) {
                    botones[f][c].setIcon(ICON_NEGRO);
                    botones[f][c].setBackground(new Color(25, 92, 13));
                    botones[f][c].setText("");
                    conteoNegras++;
                } else if ("BLANCO".equals(colorFicha)) {
                    botones[f][c].setIcon(ICON_BLANCO);
                    botones[f][c].setBackground(new Color(25, 92, 13));
                    botones[f][c].setText("");
                    conteoBlancas++;
                } else {
                    // vacío: quitamos icono y dejamos el color verde del botón
                    botones[f][c].setIcon(null);
                    botones[f][c].setBackground(new Color(25, 92, 13));
                    botones[f][c].setText("");
                }
            }
        }

        // Actualizamos labels usando los valores calculados y los getters del controlador
        conteoNegroLabel.setText("Fichas Negro: " + conteoNegras);
        conteoBlancoLabel.setText("Fichas Blanco: " + conteoBlancas);
        // Mostramos el turno en español usando el método getColor() del enum
        // "Turno actual: NEGRO" o "Turno actual: BLANCO"
        estadoLabel.setText("Turno actual: " + (controlador.getTurnoActual() != null ? controlador.getTurnoActual().getColor() : "VACIO"));

        // Actualizamos también los nombres en el panel lateral por si cambiaron
        String nombreNegro = (controlador.getJugadorNegro() != null) ? controlador.getJugadorNegro().getNombre() : "Negro";
        String nombreBlanco = (controlador.getJugadorBlanco() != null) ? controlador.getJugadorBlanco().getNombre() : "Blanco";
        jugadorNegroLabel.setText("Jugador Negro: " + nombreNegro);
        jugadorBlancoLabel.setText("Jugador Blanco: " + nombreBlanco);
    }

    // Clase interna para dibujar iconos de fichas directamente con Java2D
    // "DiscIcon" dibuja: fondo verde tipo tapete y, según el estado, una ficha negra grande,
    // una ficha blanca grande, o (estado VACIO) dos fichas pequeñas (negra y blanca) como muestra.
    private static class DiscIcon implements Icon {
        private final String tipo; // "NEGRO", "BLANCO" o "VACIO"
        private final int size; // tamaño del icono (cuadrado)

        public DiscIcon(String tipo) {
            this.tipo = tipo;
            this.size = 56; // un poco menos que el preferred size del botón
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            // Hacemos una copia del Graphics para no tocar el original
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                // Antialias para círculos suaves
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if ("NEGRO".equals(tipo)) {
                    // Ficha negra grande centrada
                    drawCenteredDisc(g2, x, y, getIconWidth(), getIconHeight(), Color.BLACK);
                } else if ("BLANCO".equals(tipo)) {
                    // Ficha blanca grande centrada
                    drawCenteredDisc(g2, x, y, getIconWidth(), getIconHeight(), Color.WHITE);
                }
            } finally {
                g2.dispose();
            }
        }

        // dibuja una ficha grande centrada dentro del rectángulo
        private void drawCenteredDisc(Graphics2D g2, int x, int y, int w, int h, Color color) {
            int diam = Math.min(w, h) * 3 / 4; // diámetro un poco menor que el icono
            int cx = x + (w - diam) / 2;
            int cy = y + (h - diam) / 2;
            g2.setColor(color);
            g2.fillOval(cx, cy, diam, diam);
            // borde ligero para dar relieve (si es blanca, usar gris para que se vea)
            g2.setColor(color == Color.WHITE ? Color.LIGHT_GRAY : Color.BLACK);
            g2.drawOval(cx, cy, diam, diam);
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
