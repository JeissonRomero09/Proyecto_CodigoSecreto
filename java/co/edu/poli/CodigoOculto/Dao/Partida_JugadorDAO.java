/**
 * DAO encargado de la relación partida-jugador y sus estadísticas.
 */
package co.edu.poli.CodigoOculto.Dao;

import java.sql.*;

public class Partida_JugadorDAO {

	private Connection conexion;

	public Partida_JugadorDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**
	 * Guarda el resultado de un jugador en una partida.
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
	 * Cuenta las partidas jugadas por un jugador.
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
	 * Cuenta las victorias de un jugador.
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
	 * Cuenta las derrotas de un jugador.
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
	 * Calcula el porcentaje de victorias (winrate) del jugador.
	 */
	public double calcularWinrate(int jugadorId) {

		int jugadas = contarPartidas(jugadorId);

		if (jugadas == 0)
			return 0;

		int ganadas = contarVictorias(jugadorId);

		return (ganadas * 100.0) / jugadas;
	}
}