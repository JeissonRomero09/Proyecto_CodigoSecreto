/**
 * Clase encargada de gestionar el temporizador del juego.
 */
package co.edu.poli.CodigoOculto.Modelo;

public class Temporizador {

	private int tiempo;

	private final int TIEMPO_MAX = 30;

	/**
	 * Constructor del temporizador.
	 * 
	 * Inicializa el tiempo con el valor máximo permitido.
	 */
	public Temporizador() {
		reiniciar();
	}

	/**
	 * Reinicia el temporizador al valor máximo.
	 */
	public void reiniciar() {

		tiempo = TIEMPO_MAX;
	}

	/**
	 * Reduce el tiempo restante en una unidad.
	 * 
	 * Solo decrementa si el tiempo aún es mayor a cero.
	 */
	public void decrementar() {

		if (tiempo > 0) {
			tiempo--;
		}
	}

	/**
	 * Retorna el tiempo actual del temporizador.
	 */
	public int getTiempo() {
		return tiempo;
	}

	/**
	 * Retorna el tiempo en formato de dos dígitos.
	 * 
	 * Ejemplo: 05 18 30
	 */
	public String getTiempoFormateado() {

		return String.format("%02d", tiempo);
	}

	/**
	 * Indica si el tiempo ya terminó.
	 */
	public boolean tiempoAgotado() {

		return tiempo <= 0;
	}
}
