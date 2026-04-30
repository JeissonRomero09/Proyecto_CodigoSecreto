package co.edu.poli.CodigoOculto.Modelo;

public class Temporizador {

	private int tiempo;
	private final int TIEMPO_MAX = 30;

	/**
	 * Constructor del temporizador. Inicializa el tiempo con el valor máximo
	 * establecido.
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
	 * Decrementa el tiempo en una unidad si aún no llega a cero.
	 */
	public void decrementar() {
		if (tiempo > 0) {
			tiempo--;
		}
	}

	/**
	 * Retorna el tiempo actual en segundos.
	 */
	public int getTiempo() {
		return tiempo;
	}

	/**
	 * Retorna el tiempo formateado en dos dígitos (ej: 09, 15).
	 */
	public String getTiempoFormateado() {
		return String.format("%02d", tiempo);
	}

	/**
	 * Indica si el tiempo del temporizador se ha agotado.
	 */
	public boolean tiempoAgotado() {
		return tiempo <= 0;
	}
}