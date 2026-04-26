package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

public class PartidaLocal_Vs {

	private Jugador jugador1;
	private Jugador jugador2;

	private Partida partidaJ1;
	private Partida partidaJ2;

	private Jugador turnoActual;

	private boolean j1YaJugo;
	private boolean j2YaJugo;

	private String resultadoJ1;
	private String resultadoJ2;

	private Jugador ganador;

	// iniciar partida
	public void iniciar(Jugador j1, Jugador j2) {

		this.jugador1 = j1;
		this.jugador2 = j2;

		this.partidaJ1 = new Partida();
		this.partidaJ2 = new Partida();

		Random r = new Random();
		this.turnoActual = (r.nextBoolean()) ? jugador1 : jugador2;

		this.j1YaJugo = false;
		this.j2YaJugo = false;
		this.ganador = null;
	}

	// obtener partida actual
	public Partida getPartidaActual() {
		return (turnoActual == jugador1) ? partidaJ1 : partidaJ2;
	}

	// obtener turno
	public Jugador getTurnoActual() {
		return turnoActual;
	}

	// jugar turno
	public String jugarTurno() {

		String resultado = getPartidaActual().procesarIntento();

		if (turnoActual == jugador1) {
			j1YaJugo = true;
			resultadoJ1 = resultado;
		} else {
			j2YaJugo = true;
			resultadoJ2 = resultado;
		}

		if (resultado.equals("GANASTE")) {
			ganador = turnoActual;
			return "FIN";
		}

		if (j1YaJugo && j2YaJugo) {
			return "RONDA_COMPLETA";
		} else {
			cambiarTurno();
			return "SIGUE";
		}
	}

	// cambiar turno
	private void cambiarTurno() {
		turnoActual = (turnoActual == jugador1) ? jugador2 : jugador1;
	}

	// siguiente ronda
	public void siguienteRonda() {

		j1YaJugo = false;
		j2YaJugo = false;

		// alternar quien inicia la nueva ronda
		cambiarTurno();
	}

	// validar ronda completa
	public boolean ambosYaJugaron() {
		return j1YaJugo && j2YaJugo;
	}

	// resultados
	public String getResultadoJ1() {
		return resultadoJ1;
	}

	public String getResultadoJ2() {
		return resultadoJ2;
	}

	public Jugador getGanador() {
		return ganador;
	}

	public Jugador getJugador1() {
		return jugador1;
	}

	public Jugador getJugador2() {
		return jugador2;
	}
}