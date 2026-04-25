package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

public class NumeroSecreto {

	private int[] combinacion;
	private int longitud;

	public NumeroSecreto(int longitud) {
		this.longitud = longitud;
		generarCombinacion();
	}

	public void generarCombinacion() {
		combinacion = new int[longitud];
		Random r = new Random();

		for (int i = 0; i < longitud; i++) {
			combinacion[i] = r.nextInt(9) + 1;
		}
	}

	public int[] getCombinacion() {
		return combinacion;
	}

	public int getLongitud() {
		return longitud;
	}
}