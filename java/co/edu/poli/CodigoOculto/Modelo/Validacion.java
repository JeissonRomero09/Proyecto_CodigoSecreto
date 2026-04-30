package co.edu.poli.CodigoOculto.Modelo;

public class Validacion {

	/**
	 * Valida un intento del jugador comparándolo con la combinación secreta.
	 * Retorna un arreglo con los estados: VERDE, AMARILLO o GRIS.
	 */
	public static String[] validar(int[] intento, int[] combinacion) {

		int n = combinacion.length;

		String[] resultado = new String[n];
		boolean[] usado = new boolean[n];

		/**
		 * Inicializa todos los resultados como GRIS
		 */
		for (int i = 0; i < n; i++) {
			resultado[i] = "GRIS";
			usado[i] = false;
		}

		/**
		 * Marca VERDES (posición correcta)
		 */
		for (int i = 0; i < n; i++) {
			if (intento[i] == combinacion[i]) {
				resultado[i] = "VERDE";
				usado[i] = true;
			}
		}

		/**
		 * Marca AMARILLOS (existe pero en otra posición)
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