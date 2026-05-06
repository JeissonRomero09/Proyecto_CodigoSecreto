package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

/**
 * Modelo que gestiona la lógica del modo 1 vs 1 local.
 * 
 * Se encarga de: - Validar inicio de partida - Gestionar turnos - Determinar
 * ganador o empate - Coordinar las partidas individuales de cada jugador
 */
public class PartidaLocal_Vs {

	private Jugador jugador1;
	private Jugador jugador2;

	private Partida partidaJ1;
	private Partida partidaJ2;

	private Jugador turnoActual;

	private boolean j1YaJugo;
	private boolean j2YaJugo;

	private Jugador ganador;
	/**
	 * Estados posibles al iniciar la partida
	 */
	public enum EstadoInicio {
		OK, JUGADOR_NO_EXISTE, MISMO_JUGADOR
	}

	/**
	 * Valida si la partida puede iniciar
	 */
	public EstadoInicio validarInicio(Jugador j1, Jugador j2) {

		if (j2 == null)
			return EstadoInicio.JUGADOR_NO_EXISTE;

		if (j1.getId() == j2.getId())
			return EstadoInicio.MISMO_JUGADOR;

		return EstadoInicio.OK;
	}

	/**
	 * Inicializa la partida:
	 * - Asigna jugadores
	 * - Crea partidas individuales
	 * - Genera números secretos
	 * - Define turno aleatorio
	 */
	public void iniciar(Jugador j1, Jugador j2) {

		this.jugador1 = j1;
		this.jugador2 = j2;

		this.partidaJ1 = new Partida(
				jugador1,
				new NumeroSecreto(5)
		);

		this.partidaJ2 = new Partida(
				jugador2,
				new NumeroSecreto(5)
		);

		Random r = new Random();

		this.turnoActual = (r.nextBoolean())
				? jugador1
				: jugador2;

		this.j1YaJugo = false;
		this.j2YaJugo = false;
		this.ganador = null;
	}

	/**
	 * Retorna la partida del jugador actual
	 */
	public Partida getPartidaActual() {
		return (turnoActual == jugador1) ? partidaJ1 : partidaJ2;
	}

	public Jugador getTurnoActual() {
		return turnoActual;
	}

	/**
	 * Ejecuta un turno: - Procesa intento - Evalúa si hay ganador - Controla rondas
	 * y cambios de turno
	 */
	public String jugarTurno() {

		Partida actual = getPartidaActual();
		String resultado = actual.procesarIntento();

		if ("GANASTE".equals(resultado)) {
			ganador = turnoActual;
			return "FIN";
		}

		marcarJugadorActual();

		if (partidaJ1.isJuegoTerminado() && partidaJ2.isJuegoTerminado()) {

			boolean iguales = comparar();

			if (iguales) {
				return "EMPATE";
			}

			return "EMPATE";
		}

		if (j1YaJugo && j2YaJugo) {
			return "RONDA_COMPLETA";
		}

		cambiarTurno();
		return "SIGUE";
	}

	/**
	 * Compara combinaciones de ambos jugadores
	 */
	private boolean comparar() {

		int[] c1 = partidaJ1.getCombinacion();
		int[] c2 = partidaJ2.getCombinacion();

		for (int i = 0; i < c1.length; i++) {
			if (c1[i] != c2[i])
				return false;
		}
		return true;
	}

	/**
	 * Maneja el caso en que el tiempo se agota
	 */
	public String tiempoAgotado() {

		Partida p = getPartidaActual();
		String estado = p.tiempoAgotado();

		marcarJugadorActual();

		if ("GANASTE".equals(estado)) {
			ganador = turnoActual;
			return "FIN";
		}

		if (j1YaJugo && j2YaJugo) {
			return "RONDA_COMPLETA";
		}

		cambiarTurno();
		return "SIGUE";
	}

	/**
	 * Marca que el jugador actual ya jugó
	 */
	private void marcarJugadorActual() {
		if (turnoActual == jugador1) {
			j1YaJugo = true;
		} else {
			j2YaJugo = true;
		}
	}

	/**
	 * Cambia el turno entre jugadores
	 */
	private void cambiarTurno() {
		turnoActual = (turnoActual == jugador1) ? jugador2 : jugador1;
	}

	/**
	 * Reinicia estado de ronda
	 */
	public void siguienteRonda() {
		j1YaJugo = false;
		j2YaJugo = false;
		cambiarTurno();
	}

	public Partida getPartidaJ1() {
		return partidaJ1;
	}

	public Partida getPartidaJ2() {
		return partidaJ2;
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

	public int[] getCombinacionJ1() {
		return partidaJ1.getCombinacion();
	}

	public int[] getCombinacionJ2() {
		return partidaJ2.getCombinacion();
	}
}