package edu.ucr.controller;
import edu.ucr.model.Ficha;
import edu.ucr.model.Jugador;
import edu.ucr.model.Partida;
import edu.ucr.model.Tablero;
import edu.ucr.repository.IJugadorRepository;
import edu.ucr.repository.IPartidaRepository;

import javax.swing.*;

public class JuegoController {
    private Tablero tablero;
    private Jugador jugadorBlanco;
    private Jugador jugadorNegro;
    private Ficha turno;
    private IJugadorRepository jugadorRepository;
    private IPartidaRepository partidaRepository; // Corregido el nombre para consistencia
    private JPanel panelTablero;


    public JuegoController(IJugadorRepository jRepository, IPartidaRepository pRepository) {
        this.jugadorRepository = jRepository;
        this.partidaRepository = pRepository;
        this.turno = Ficha.NEGRA; // Por regla, el negro siempre inicia
    }

    // --- LÓGICA DE INICIO ---

    public void iniciarNuevaPartida(int tamano, String nombreNegro, String nombreBlanco) {
        this.tablero = new Tablero(tamano);

        // Obtenemos o creamos los jugadores
        this.jugadorNegro = obtenerOCrearJugador(nombreNegro);
        this.jugadorBlanco = obtenerOCrearJugador(nombreBlanco);

        // IMPORTANTE: Colocar las 4 fichas iniciales en el centro
        int centro1 = (tamano / 2) - 1;
        int centro2 = tamano / 2;

        tablero.setFicha(centro1, centro1, Ficha.BLANCA);
        tablero.setFicha(centro2, centro2, Ficha.BLANCA);
        tablero.setFicha(centro1, centro2, Ficha.NEGRA);
        tablero.setFicha(centro2, centro1, Ficha.NEGRA);

        this.turno = Ficha.NEGRA;
        System.out.println("Partida iniciada: " + nombreNegro + " VS " + nombreBlanco);
    }

    private Jugador obtenerOCrearJugador(String nombre) {
        if (this.jugadorRepository == null) {
            return new Jugador(nombre);
        }
        Jugador jugador = jugadorRepository.obtenerPorNombre(nombre);
        if (jugador == null) {
            jugador = new Jugador(nombre);
            jugadorRepository.guardar(jugador);
        }
        return jugador;
    }

    // --- LÓGICA DE MOVIMIENTO ---

    //CAMBIO: ejecutarMovimiento ahora devuelve ResultadoOperacion para explicar el resultado
    public edu.ucr.controller.ResultadoOperacion ejecutarMovimiento(int fila, int columna) {
        try {
            // 1) Validaciones básicas
            if (tablero == null) return edu.ucr.controller.ResultadoOperacion.ERROR; // tablero no inicializado
            if (!tablero.esPosicionValida(fila, columna)) {
                return edu.ucr.controller.ResultadoOperacion.INVALID_POSITION;
            }

            // 2) Casilla vacía?
            edu.ucr.model.Ficha fichaActual = tablero.getFicha(fila, columna);
            if (fichaActual != edu.ucr.model.Ficha.VACIA && fichaActual != null) {
                return edu.ucr.controller.ResultadoOperacion.CELL_NOT_EMPTY;
            }

            // 3) Movimiento válido (captura al menos en una dirección)?
            if (!esMovimientoValido(fila, columna)) {
                return edu.ucr.controller.ResultadoOperacion.INVALID_MOVE;
            }

            // 4) Aplicar movimiento y voltear fichas
            tablero.setFicha(fila, columna, this.turno);
            voltearFichas(fila, columna);

            // 5) Cambiar turno
            cambiarTurno();
            return edu.ucr.controller.ResultadoOperacion.SUCCESS;
        } catch (Exception ex) {
            // En lugar de swallow: registramos y devolvemos ERROR
            ex.printStackTrace(); // más adelante cambiar por Logger
            return edu.ucr.controller.ResultadoOperacion.ERROR;
        }
    }

    private boolean esMovimientoValido(int fila, int columna) {
        // Definimos las 8 direcciones posibles (Vertical, Horizontal, Diagonal)
        int[] dirFila = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dirCol  = {-1, 0, 1, -1, 1, -1, 0, 1};

        // Revisamos cada dirección
        for (int i = 0; i < 8; i++) {
            if (puedeCapturarEnDireccion(fila, columna, dirFila[i], dirCol[i])) {
                return true; // Con que pueda capturar en una dirección, el movimiento es válido
            }
        }
        return false;
    }

    // Este método revisa una dirección específica para ver si hay capturas
    private boolean puedeCapturarEnDireccion(int fila, int col, int dF, int dC) {
        Ficha oponente = (this.turno == Ficha.NEGRA) ? Ficha.BLANCA : Ficha.NEGRA;
        int f = fila + dF;
        int c = col + dC;
        boolean hayOponenteEnMedio = false;

        while (tablero.esPosicionValida(f, c)) {
            Ficha fichaActual = tablero.getFicha(f, c);

            if (fichaActual == oponente) {
                hayOponenteEnMedio = true;
            } else if (fichaActual == this.turno) {
                return hayOponenteEnMedio; // Encontramos una ficha propia al final
            } else {
                return false; // Casilla vacía, no se encierra nada
            }
            f += dF;
            c += dC;
        }
        return false;
    }

    // Este método es el que "voltea" las fichas físicamente en el tablero
    private void voltearFichas(int fila, int col) {
        int[] dirFila = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dirCol  = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            if (puedeCapturarEnDireccion(fila, col, dirFila[i], dirCol[i])) {
                int f = fila + dirFila[i];
                int c = col + dirCol[i];

                // Mientras sea del oponente, la cambiamos a nuestro color
                while (tablero.getFicha(f, c) != this.turno) {
                    tablero.setFicha(f, c, this.turno);
                    f += dirFila[i];
                    c += dirCol[i];
                }
            }
        }
    }

    private void cambiarTurno() {
        this.turno = (this.turno == Ficha.NEGRA) ? Ficha.BLANCA : Ficha.NEGRA;
    }

    // --- MÉTODOS PARA LA INTERFAZ (VIEW) ---

    public String getColorEnPosicion(int fila, int col) {
        Ficha ficha = tablero.getFicha(fila, col);
        if (ficha == null || ficha == Ficha.VACIA) {
            return "VACIO";
        }
        return ficha.getColor(); // Usa el método getColor() que arreglamos en el Enum
    }

    public Ficha getTurnoActual() {
        return this.turno;
    }

    // Cambios añadidos: métodos que la vista necesita para ser dinámica
    // Explicación en lenguaje cotidiano: estos métodos permiten que la interfaz pregunte
    // de qué tamaño es el tablero, quiénes son los jugadores y cuántas fichas tiene cada uno.
    public int getTamañoTablero() {
        // Si aún no existe tablero por alguna razón, devolvemos 8 por defecto
        return (this.tablero != null) ? this.tablero.getTamaño() : 8;
    }

    public Jugador getJugadorNegro() {
        return this.jugadorNegro;
    }

    public Jugador getJugadorBlanco() {
        return this.jugadorBlanco;
    }

    public int getConteoNegro() {
        if (this.tablero == null) return 0;
        int contador = 0;
        int n = this.tablero.getTamaño();
        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                if (this.tablero.getFicha(f, c) == Ficha.NEGRA) contador++;
            }
        }
        return contador;
    }

    public int getConteoBlanco() {
        if (this.tablero == null) return 0;
        int contador = 0;
        int n = this.tablero.getTamaño();
        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                if (this.tablero.getFicha(f, c) == Ficha.BLANCA) contador++;
            }
        }
        return contador;
    }

    // --- PERSISTENCIA ---

    public void guardarProgreso(String ruta) {
        if (partidaRepository != null) {
            Partida partidaActual = new Partida(tablero, jugadorNegro, jugadorBlanco, turno);
            partidaRepository.guardarPartida(partidaActual, ruta);
        }
    }

    // Nuevo: cargar una partida desde archivo y aplicar su estado al controlador
    // En lenguaje cotidiano: esto lee una Partida del archivo y actualiza el tablero y jugadores en memoria
    public boolean cargarPartidaDesdeArchivo(String ruta) {
        if (partidaRepository == null) return false;
        Partida p = partidaRepository.cargarPartida(ruta);
        if (p == null) return false;
        // Aplicamos el estado
        this.tablero = p.getTablero();
        this.jugadorNegro = p.getJugadorNegro();
        this.jugadorBlanco = p.getJugadorBlanco();
        this.turno = p.getTurno();
        return true;
    }
}