package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

/**
 * clase encargada de generar y almacenar el numero secreto utilizado durante la
 * partida
 */
public class NumeroSecreto {

	private int[] combinacion;
	private int longitud;

	/**
	 * constructor que inicializa la longitud del codigo secreto y genera
	 * automaticamente la combinacion aleatoria
	 * 
	 * @param longitud tamaño del codigo
	 */
	public NumeroSecreto(int longitud) {
		this.longitud = longitud;
		generarCombinacion();
	}

	/**
	 * genera una combinacion aleatoria utilizando numeros entre 1 y 9 segun la
	 * longitud indicada
	 */
	public void generarCombinacion() {

		combinacion = new int[longitud];

		Random r = new Random();

		for (int i = 0; i < longitud; i++) {
			combinacion[i] = r.nextInt(9) + 1;
		}
	}

	/**
	 * retorna la combinacion secreta generada para la partida
	 * 
	 * @return arreglo con la combinacion
	 */
	public int[] getCombinacion() {
		return combinacion;
	}

	/**
	 * retorna la longitud definida para el numero secreto
	 * 
	 * @return tamaño del codigo
	 */
	public int getLongitud() {
		return longitud;
	}
}
