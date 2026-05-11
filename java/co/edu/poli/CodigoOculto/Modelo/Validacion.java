/**
 * Clase encargada de validar los intentos realizados
 * por el jugador comparándolos con la combinación secreta.
 */
package co.edu.poli.CodigoOculto.Modelo;

public class Validacion {

	/**
	 * Valida un intento del jugador contra la combinación secreta.
	 * 
	 * Retorna un arreglo con los siguientes estados:
	 * 
	 * VERDE: Número correcto en la posición correcta.
	 * 
	 * AMARILLO: Número existente en la combinación pero en otra posición.
	 * 
	 * GRIS: Número que no existe en la combinación secreta.
	 */
	public static String[] validar(int[] intento, int[] combinacion) {

		int n = combinacion.length;

		String[] resultado = new String[n];

		boolean[] usado = new boolean[n];

		/**
		 * Inicializa todos los resultados como GRIS.
		 */
		for (int i = 0; i < n; i++) {

			resultado[i] = "GRIS";

			usado[i] = false;
		}

		/**
		 * Primera validación: Detecta coincidencias exactas (VERDE).
		 */
		for (int i = 0; i < n; i++) {

			if (intento[i] == combinacion[i]) {

				resultado[i] = "VERDE";

				usado[i] = true;
			}
		}

		/**
		 * Segunda validación: Detecta números existentes en otra posición (AMARILLO).
		 */
		for (int i = 0; i < n; i++) {

			if (!resultado[i].equals("VERDE")) {

				for (int j = 0; j < n; j++) {

					if (!usado[j] && intento[i] == combinacion[j]) {

						resultado[i] = "AMARILLO";

						usado[j] = true;

						break;
					}
				}
			}
		}

		return resultado;
	}
}
