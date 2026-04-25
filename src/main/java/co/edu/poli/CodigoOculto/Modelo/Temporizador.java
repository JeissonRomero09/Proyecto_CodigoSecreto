package co.edu.poli.CodigoOculto.Modelo;

public class Temporizador {

	private int tiempo;
	private final int TIEMPO_MAX = 30;

	public Temporizador() {
		reiniciar();
	}

	public void reiniciar() {
		tiempo = TIEMPO_MAX;
	}

	public void decrementar() {
		if (tiempo > 0) {
			tiempo--;
		}
	}

	public int getTiempo() {
		return tiempo;
	}

	public String getTiempoFormateado() {
		return String.format("%02d", tiempo);
	}

	public boolean tiempoAgotado() {
		return tiempo <= 0;
	}
}