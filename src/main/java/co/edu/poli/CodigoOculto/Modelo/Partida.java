package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

public class Partida {

	private final int MAX_FILAS = 5;
	private final int MAX_COLUMNAS = 5;

	private String[][] tablero;
	private int filaActual;
	private int columnaActual;
	private boolean juegoTerminado;
	private String[] ultimoResultado;

	private int[] combinacion;
	private int filaEvaluada;

	public Partida() {
		iniciarPartida();
	}
	public String tiempoAgotado() {

	   
	    completarFilaConCeros();

	  
	    filaEvaluada = filaActual;


	    if (!intentarPasarDeFila()) {
	        juegoTerminado = true;
	        return "PERDISTE";
	    }

	    return "CONTINUA";
	}

	public String procesarIntento() {

		if (!esFilaCompleta())
			return "INCOMPLETO";

		filaEvaluada = filaActual;

		ultimoResultado = validarFilaActual();

		if (esVictoria(ultimoResultado)) {
			juegoTerminado = true;
			return "GANASTE";
		}

		if (!intentarPasarDeFila()) {
			juegoTerminado = true;
			return "PERDISTE";
		}

		return "CONTINUA";
	}

	public void completarFilaConCeros() {

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			if (tablero[filaActual][i] == null || tablero[filaActual][i].isEmpty()) {
				tablero[filaActual][i] = "0";
			}
		}
	}

	public String getValorCelda(int fila, int col) {
		return tablero[fila][col];
	}

	public String[] getUltimoResultado() {
		return ultimoResultado;
	}

	public int getFilaEvaluada() {
		return filaEvaluada;
	}

	public void iniciarPartida() {
		this.tablero = new String[MAX_FILAS][MAX_COLUMNAS];
		this.filaActual = 0;
		this.columnaActual = 0;
		this.juegoTerminado = false;
		generarCombinacion();
	}

	private void generarCombinacion() {
		combinacion = new int[MAX_COLUMNAS];
		Random r = new Random();

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			combinacion[i] = r.nextInt(9) + 1;
		}
	}

	public boolean realizarIntento(String numero) {

		if (juegoTerminado || columnaActual >= MAX_COLUMNAS)
			return false;

		tablero[filaActual][columnaActual] = numero;
		avanzarCursorInterno();
		return true;
	}

	private void avanzarCursorInterno() {
		if (columnaActual < MAX_COLUMNAS - 1) {
			columnaActual++;
		}
	}

	public String[] validarFilaActual() {

		int[] intento = new int[MAX_COLUMNAS];

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			intento[i] = Integer.parseInt(tablero[filaEvaluada][i]);
		}

		return Validacion.validar(intento, combinacion);
	}

	public boolean esVictoria(String[] resultado) {
		for (String r : resultado) {
			if (!r.equals("VERDE"))
				return false;
		}
		return true;
	}

	public boolean intentarPasarDeFila() {

		if (filaActual < MAX_FILAS - 1) {
			filaActual++;
			columnaActual = 0;
			return true;
		} else {
			finalizarPartida();
			return false;
		}
	}
	public void setFilaEvaluada(int fila) {
		this.filaEvaluada = fila;
	}
	public int[] getCombinacion() {
		return combinacion;
	}

	public void finalizarPartida() {
		this.juegoTerminado = true;
	}

	public boolean esFilaCompleta() {

		for (int i = 0; i < MAX_COLUMNAS; i++) {
			if (tablero[filaActual][i] == null || tablero[filaActual][i].isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void moverCursorManual(int col) {
		if (!juegoTerminado) {
			this.columnaActual = col;
		}
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
}