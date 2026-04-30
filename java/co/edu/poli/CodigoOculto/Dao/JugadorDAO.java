/**
 * DAO encargado de las operaciones de la tabla jugador.
 */
package co.edu.poli.CodigoOculto.Dao;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import java.sql.*;

public class JugadorDAO {

	private Connection conexion;

	public JugadorDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**
	 * Busca un jugador por su ID.
	 */
	public Jugador buscarJugadorPorId(int id) {

		String sql = "SELECT * FROM jugador WHERE id = ?";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getInt("puntaje"),
						rs.getBoolean("es_invitado"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Guarda un nuevo jugador.
	 */
	public boolean guardarJugador(Jugador jugador) {

		String sql = "INSERT INTO jugador (nombre, puntaje, es_invitado) VALUES (?, ?, ?)";

		try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, jugador.getNombre());
			ps.setInt(2, jugador.getPuntaje());
			ps.setBoolean(3, jugador.isEsInvitado());

			int filas = ps.executeUpdate();

			if (filas > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					jugador.setId(rs.getInt(1));
				}
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Actualiza el puntaje de un jugador.
	 */
	public boolean actualizarPuntaje(Jugador jugador) {

		String sql = "UPDATE jugador SET puntaje = ? WHERE id = ?";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, jugador.getPuntaje());
			ps.setInt(2, jugador.getId());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Verifica si existe un jugador con ese nombre.
	 */
	public boolean existeNombre(String nombre) {

		String sql = "SELECT id FROM jugador WHERE nombre = ?";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}