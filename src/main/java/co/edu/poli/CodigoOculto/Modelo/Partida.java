package co.edu.poli.CodigoOculto.Modelo;

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

	public Partida() {
		iniciarPartida();
	}

	public void iniciarPartida() {
		this.tablero = new String[MAX_FILAS][MAX_COLUMNAS];
		this.filaActual = 0;
		this.columnaActual = 0;
		this.juegoTerminado = false;
		this.numeroSecreto = new NumeroSecreto(MAX_COLUMNAS);
	}

	// insertar numero
	public boolean realizarIntento(String numero) {

		if (juegoTerminado || columnaActual >= MAX_COLUMNAS)
			return false;

		tablero[filaActual][columnaActual] = numero;
		avanzarCursor();
		return true;
	}

	private void avanzarCursor() {
		if (columnaActual < MAX_COLUMNAS - 1) {
			columnaActual++;
		}
	}

	// procesar intento
	public String procesarIntento() {

		if (!esFilaCompleta())
			return "INCOMPLETO";

		filaEvaluada = filaActual;

		int[] intento = obtenerIntentoActual();

		ultimoResultado = Validacion.validar(
			intento,
			numeroSecreto.getCombinacion()
		);

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

	private int[] obtenerIntentoActual() {

		int[] intento = new int[MAX_COLUMNAS];

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			intento[i] = Integer.parseInt(tablero[filaActual][i]);
		}

		return intento;
	}

	private boolean esVictoria() {
		for (String r : ultimoResultado) {
			if (!r.equals("VERDE"))
				return false;
		}
		return true;
	}

	private boolean pasarFila() {

		if (filaActual < MAX_FILAS - 1) {
			filaActual++;
			columnaActual = 0;
			return true;
		}

		return false;
	}

	public boolean esFilaCompleta() {

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			if (tablero[filaActual][i] == null || tablero[filaActual][i].isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void completarFilaConCeros() {

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			if (tablero[filaActual][i] == null || tablero[filaActual][i].isEmpty()) {
				tablero[filaActual][i] = "0";
			}
		}
	}

	public String tiempoAgotado() {

		completarFilaConCeros();

		filaEvaluada = filaActual;

		if (!pasarFila()) {
			juegoTerminado = true;
			return "PERDISTE";
		}

		return "CONTINUA";
	}
	
	public void moverCursorManual(int col) {

	    if (!juegoTerminado && col >= 0 && col < MAX_COLUMNAS) {
	        this.columnaActual = col;
	    }
	}

	// getters

	public String getValorCelda(int fila, int col) {
		return tablero[fila][col];
	}

	public String[] getUltimoResultado() {
		return ultimoResultado;
	}

	public int getFilaActual() {
		return filaActual;
	}

	public int getColumnaActual() {
		return columnaActual;
	}

	public boolean isJuegoTerminado() {
		return juegoTerminado;
	}

	public int[] getCombinacion() {
		return numeroSecreto.getCombinacion();
	}

	public int getFilaEvaluada() {
		return filaEvaluada;
	}
}