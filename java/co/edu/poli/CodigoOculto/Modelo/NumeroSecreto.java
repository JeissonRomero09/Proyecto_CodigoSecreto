package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

public class NumeroSecreto {

	private int[] combinacion;
	private int longitud;

	/**
	 * Constructor de la clase NumeroSecreto. Inicializa la longitud del número
	 * secreto y genera la combinación.
	 */
	public NumeroSecreto(int longitud) {
		this.longitud = longitud;
		generarCombinacion();
	}

	/**
	 * Genera una combinación aleatoria de números entre 1 y 9.
	 */
	public void generarCombinacion() {
		combinacion = new int[longitud];
		Random r = new Random();

		for (int i = 0; i < longitud; i++) {
			combinacion[i] = r.nextInt(9) + 1;
		}
	}

	/**
	 * Retorna la combinación secreta generada.
	 */
	public int[] getCombinacion() {
		return combinacion;
	}

	/**
	 * Retorna la longitud del número secreto.
	 */
	public int getLongitud() {
		return longitud;
	}
}