package co.edu.poli.CodigoOculto.Modelo;

/**
 * clase encargada de gestionar
 * la logica principal de una partida
 * individual del juego
 */
public class Partida {

	private final int MAX_FILAS = 5;
	private final int MAX_COLUMNAS = 5;

	private String[][] tablero;
	private int filaActual;
	private int columnaActual;
	private boolean juegoTerminado;

	private String[] ultimoResultado;

	private NumeroSecreto numeroSecreto;

	private int filaEvaluada;

	private Jugador jugador;

	/**
	 * inicializa la partida con
	 * el jugador y el numero secreto
	 * utilizado durante el juego
	 * @param jugador jugador actual
	 * @param numeroSecreto codigo secreto
	 */
	public Partida(Jugador jugador, NumeroSecreto numeroSecreto) {

		this.jugador = jugador;
		this.numeroSecreto = numeroSecreto;

		this.tablero = new String[MAX_FILAS][MAX_COLUMNAS];
		this.filaActual = 0;
		this.columnaActual = 0;
		this.juegoTerminado = false;
	}

	/**
	 * registra un numero en la
	 * posicion actual del tablero
	 * y avanza el cursor
	 * @param numero numero ingresado
	 * @return true si el intento fue valido
	 */
	public boolean realizarIntento(String numero) {

		if (juegoTerminado || columnaActual >= MAX_COLUMNAS)
			return false;

		tablero[filaActual][columnaActual] = numero;

		avanzarCursor();

		return true;
	}

	/**
	 * mueve el cursor a la
	 * siguiente columna disponible
	 */
	private void avanzarCursor() {

		if (columnaActual < MAX_COLUMNAS - 1) {
			columnaActual++;
		}
	}

	/**
	 * procesa el intento realizado
	 * por el jugador en la fila actual
	 * @return estado actual de la partida
	 */
	public String procesarIntento() {

		if (!esFilaCompleta())
			return "INCOMPLETO";

		filaEvaluada = filaActual;

		int[] intento = obtenerIntentoActual();

		ultimoResultado = Validacion.validar(
				intento,
				numeroSecreto.getCombinacion());

		if (esVictoria()) {
			juegoTerminado = true;
			return "GANASTE";
		}

		if (!pasarFila()) {
			juegoTerminado = true;
			return "PERDISTE";
		}

		return "CONTINUA";
	}

	/**
	 * obtiene la fila actual
	 * convertida en arreglo numerico
	 * @return intento actual del jugador
	 */
	private int[] obtenerIntentoActual() {

		int[] intento = new int[MAX_COLUMNAS];

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			intento[i] = Integer.parseInt(tablero[filaActual][i]);
		}

		return intento;
	}

	/**
	 * verifica si el resultado
	 * actual corresponde a una victoria
	 * @return true si gano la partida
	 */
	private boolean esVictoria() {

		for (String r : ultimoResultado) {

			if (!r.equals("VERDE"))
				return false;
		}

		return true;
	}

	/**
	 * avanza a la siguiente fila
	 * disponible del tablero
	 * @return true si pudo avanzar
	 */
	private boolean pasarFila() {

		if (filaActual < MAX_FILAS - 1) {

			filaActual++;
			columnaActual = 0;

			return true;
		}

		return false;
	}

	/**
	 * verifica si la fila actual
	 * contiene todos los valores
	 * necesarios para jugar
	 * @return true si esta completa
	 */
	public boolean esFilaCompleta() {

		for (int i = 0; i < MAX_COLUMNAS; i++) {

			if (tablero[filaActual][i] == null
					|| tablero[filaActual][i].isEmpty()) {

				return false;
			}
		}

		return true;
	}

	/**
	 * completa con ceros las casillas
	 * vacias de la fila actual
	 */
	public void completarFilaConCeros() {

		for (int i = 0; i < MAX_COLUMNAS; i++) {

			if (tablero[filaActual][i] == null
					|| tablero[filaActual][i].isEmpty()) {

				tablero[filaActual][i] = "0";
			}
		}
	}

	/**
	 * ejecuta la accion correspondiente
	 * cuando el tiempo del jugador termina
	 * @return estado actual de la partida
	 */
	public String tiempoAgotado() {

		completarFilaConCeros();

		filaEvaluada = filaActual;

		if (!pasarFila()) {
			juegoTerminado = true;
			return "PERDISTE";
		}

		return "CONTINUA";
	}

	/**
	 * mueve manualmente el cursor
	 * a una columna especifica
	 * @param col columna seleccionada
	 */
	public void moverCursorManual(int col) {

		if (!juegoTerminado
				&& col >= 0
				&& col < MAX_COLUMNAS) {

			this.columnaActual = col;
		}
	}

	/**
	 * retorna el valor almacenado
	 * en una casilla del tablero
	 * @param fila fila de la casilla
	 * @param col columna de la casilla
	 * @return valor guardado en la casilla
	 */
	public String getValorCelda(int fila, int col) {
		return tablero[fila][col];
	}

	/**
	 * retorna el ultimo resultado
	 * generado por la validacion
	 * @return arreglo de resultados
	 */
	public String[] getUltimoResultado() {
		return ultimoResultado;
	}

	/**
	 * retorna la fila actual
	 * utilizada en la partida
	 * @return fila actual
	 */
	public int getFilaActual() {
		return filaActual;
	}

	/**
	 * retorna la columna actual
	 * donde se escribe el intento
	 * @return columna actual
	 */
	public int getColumnaActual() {
		return columnaActual;
	}

	/**
	 * indica si la partida
	 * ya finalizo completamente
	 * @return true si termino
	 */
	public boolean isJuegoTerminado() {
		return juegoTerminado;
	}

	/**
	 * retorna la combinacion secreta
	 * utilizada en la partida
	 * @return codigo secreto
	 */
	public int[] getCombinacion() {
		return numeroSecreto.getCombinacion();
	}

	/**
	 * retorna la ultima fila
	 * evaluada del tablero
	 * @return fila evaluada
	 */
	public int getFilaEvaluada() {
		return filaEvaluada;
	}
}
