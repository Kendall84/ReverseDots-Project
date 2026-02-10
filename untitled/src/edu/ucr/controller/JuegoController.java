package edu.ucr.controller;
import edu.ucr.model.Ficha;
import edu.ucr.model.Jugador;
import edu.ucr.model.Partida;
import edu.ucr.model.Tablero;
import edu.ucr.repository.IJugadorRepository;
import edu.ucr.repository.IPartidaRepository;

public class JuegoController {
    private Tablero tablero;
    private Jugador jugadorBlanco;
    private Jugador jugadorNegro;
    private Ficha turno;
    private IJugadorRepository jugadorRepository;
    private IPartidaRepository partidaRepository; // Corregido el nombre para consistencia

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

    public boolean ejecutarMovimiento(int fila, int columna) {
        // 1. Validaciones básicas (Tablero y Celda vacía)
        if (!tablero.esPosicionValida(fila, columna)) return false;

        Ficha fichaActual = tablero.getFicha(fila, columna);
        if (fichaActual != Ficha.VACIA && fichaActual != null) return false;

        // 2. IMPORTANTE: Validar si el movimiento captura algo
        // Ahora esMovimientoValido ya no siempre devuelve true
        if (!esMovimientoValido(fila, columna)) {
            return false;
        }

        // 3. Si es válido, ponemos la ficha y HACEMOS LAS CAPTURAS
        tablero.setFicha(fila, columna, this.turno);
        voltearFichas(fila, columna); // <--- ¡Nuevo método!

        // 4. Cambiar turno
        cambiarTurno();
        return true;
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

    // --- PERSISTENCIA ---

    public void guardarProgreso(String ruta) {
        if (partidaRepository != null) {
            Partida partidaActual = new Partida(tablero, jugadorNegro, jugadorBlanco, turno);
            partidaRepository.guardarPartida(partidaActual, ruta);
        }
    }
}
