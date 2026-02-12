package edu.ucr.ucr.controller;
// Resultado de una operación de juego: explicado en lenguaje cotidiano
// - SUCCESS: la jugada se realizó correctamente
// - INVALID_POSITION: la casilla está fuera del tablero
// - CELL_NOT_EMPTY: la casilla ya tiene una ficha
// - INVALID_MOVE: la jugada no captura fichas (no es válida según las reglas)
// - ERROR: cualquier otro error inesperado
/*
  ResultadoOperacion: enum con los posibles resultados al intentar hacer un movimiento.
  Comentario en lenguaje cotidiano: devuelve por qué falló o si tuvo éxito, eso facilita
  la interacción entre controlador y vista.
*/
public enum ResultadoOperacion {
    SUCCESS,            // La jugada se realizó y el estado cambió
    INVALID_POSITION,   // La posición está fuera del tablero
    CELL_NOT_EMPTY,     // La celda ya tiene ficha
    INVALID_MOVE,       // La jugada no captura ninguna ficha (no es válida)
    ERROR               // Algo inesperado ocurrió (p. ej. NullPointer)
}
