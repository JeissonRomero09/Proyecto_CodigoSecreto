package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

/**
 * modelo encargado de gestionar
 * la logica del modo 1 vs 1 local
 * entre dos jugadores
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
	 * estados posibles utilizados
	 * durante la validacion inicial
	 * de la partida
	 */
	public enum EstadoInicio {
		OK,
		JUGADOR_NO_EXISTE,
		MISMO_JUGADOR
	}

	/**
	 * valida si ambos jugadores
	 * cumplen las condiciones
	 * necesarias para iniciar
	 * la partida local
	 * @param j1 jugador 1
	 * @param j2 jugador 2
	 * @return estado de validacion
	 */
	public EstadoInicio validarInicio(Jugador j1, Jugador j2) {

		if (j2 == null)
			return EstadoInicio.JUGADOR_NO_EXISTE;

		if (j1.getId() == j2.getId())
			return EstadoInicio.MISMO_JUGADOR;

		return EstadoInicio.OK;
	}

	/**
	 * inicializa completamente
	 * la partida asignando
	 * jugadores turnos y
	 * numeros secretos
	 * @param j1 jugador 1
	 * @param j2 jugador 2
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
	 * retorna la partida que
	 * corresponde al jugador
	 * que tiene el turno actual
	 * @return partida activa
	 */
	public Partida getPartidaActual() {
		return (turnoActual == jugador1)
				? partidaJ1
				: partidaJ2;
	}

	/**
	 * retorna el jugador que
	 * posee el turno actual
	 * @return jugador actual
	 */
	public Jugador getTurnoActual() {
		return turnoActual;
	}

	/**
	 * procesa el turno del jugador
	 * actual evaluando resultados
	 * cambios de turno y ganador
	 * @return estado de la partida
	 */
	public String jugarTurno() {

		Partida actual = getPartidaActual();

		String resultado = actual.procesarIntento();

		if ("GANASTE".equals(resultado)) {

			ganador = turnoActual;

			return "FIN";
		}

		marcarJugadorActual();

		if (partidaJ1.isJuegoTerminado()
				&& partidaJ2.isJuegoTerminado()) {

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
	 * compara las combinaciones
	 * secretas de ambos jugadores
	 * @return true si son iguales
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
	 * procesa la accion cuando
	 * el tiempo del jugador
	 * actual se agota
	 * @return estado de la partida
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
	 * marca que el jugador
	 * actual ya realizo
	 * su turno en la ronda
	 */
	private void marcarJugadorActual() {

		if (turnoActual == jugador1) {
			j1YaJugo = true;
		}

		else {
			j2YaJugo = true;
		}
	}

	/**
	 * cambia el turno entre
	 * ambos jugadores
	 */
	private void cambiarTurno() {

		turnoActual = (turnoActual == jugador1)
				? jugador2
				: jugador1;
	}

	/**
	 * reinicia el estado de
	 * la ronda y cambia
	 * el turno inicial
	 */
	public void siguienteRonda() {

		j1YaJugo = false;
		j2YaJugo = false;

		cambiarTurno();
	}

	/**
	 * retorna la partida
	 * correspondiente al jugador 1
	 * @return partida jugador 1
	 */
	public Partida getPartidaJ1() {
		return partidaJ1;
	}

	/**
	 * retorna la partida
	 * correspondiente al jugador 2
	 * @return partida jugador 2
	 */
	public Partida getPartidaJ2() {
		return partidaJ2;
	}

	/**
	 * retorna el jugador ganador
	 * de la partida actual
	 * @return jugador ganador
	 */
	public Jugador getGanador() {
		return ganador;
	}

	/**
	 * retorna el jugador 1
	 * registrado en la partida
	 * @return jugador 1
	 */
	public Jugador getJugador1() {
		return jugador1;
	}

	/**
	 * retorna el jugador 2
	 * registrado en la partida
	 * @return jugador 2
	 */
	public Jugador getJugador2() {
		return jugador2;
	}

	/**
	 * retorna la combinacion
	 * secreta del jugador 1
	 * @return codigo jugador 1
	 */
	public int[] getCombinacionJ1() {
		return partidaJ1.getCombinacion();
	}

	/**
	 * retorna la combinacion
	 * secreta del jugador 2
	 * @return codigo jugador 2
	 */
	public int[] getCombinacionJ2() {
		return partidaJ2.getCombinacion();
	}
}
