package co.edu.poli.CodigoOculto.Dao;

import java.sql.*;

/**
 * dao encargado de gestionar la relacion entre partidas jugadores y
 * estadisticas
 */
public class Partida_JugadorDAO {

	private Connection conexion;

	/**
	 * recibe la conexion utilizada para realizar operaciones sql sobre la base de
	 * datos
	 * 
	 * @param conexion conexion activa
	 */
	public Partida_JugadorDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**
	 * guarda el resultado obtenido por un jugador en una partida especifica del
	 * sistema
	 * 
	 * @param partidaId id de la partida
	 * @param jugadorId id del jugador
	 * @param resultado resultado obtenido
	 * @return true si se guardo correctamente
	 */
	public boolean guardar(int partidaId, int jugadorId, String resultado) {

		String sql = "INSERT INTO partida_jugador (partida_id, jugador_id, resultado) VALUES (?, ?, ?)";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, partidaId);
			ps.setInt(2, jugadorId);
			ps.setString(3, resultado);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * cuenta la cantidad total de partidas jugadas por un jugador especifico
	 * 
	 * @param jugadorId id del jugador
	 * @return total de partidas jugadas
	 */
	public int contarPartidas(int jugadorId) {

		String sql = "SELECT COUNT(*) FROM partida_jugador WHERE jugador_id = ?";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, jugadorId);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * cuenta la cantidad total de victorias registradas para un jugador
	 * 
	 * @param jugadorId id del jugador
	 * @return total de victorias
	 */
	public int contarVictorias(int jugadorId) {

		String sql = "SELECT COUNT(*) FROM partida_jugador WHERE jugador_id = ? AND resultado = 'VICTORIA'";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, jugadorId);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * cuenta la cantidad total de derrotas registradas para un jugador
	 * 
	 * @param jugadorId id del jugador
	 * @return total de derrotas
	 */
	public int contarDerrotas(int jugadorId) {

		String sql = "SELECT COUNT(*) FROM partida_jugador WHERE jugador_id = ? AND resultado = 'DERROTA'";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, jugadorId);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * calcula el porcentaje de victorias obtenidas por un jugador registrado
	 * 
	 * @param jugadorId id del jugador
	 * @return porcentaje de victorias
	 */
	public double calcularWinrate(int jugadorId) {

		int jugadas = contarPartidas(jugadorId);

		if (jugadas == 0)
			return 0;

		int ganadas = contarVictorias(jugadorId);

		return (ganadas * 100.0) / jugadas;
	}
}
