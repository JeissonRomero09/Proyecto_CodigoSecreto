package co.edu.poli.CodigoOculto.Dao;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import java.sql.*;

/**
 * dao encargado de gestionar las operaciones de la tabla jugador
 */
public class JugadorDAO {

	private Connection conexion;

	/**
	 * recibe y almacena la conexion utilizada para las consultas sql
	 * 
	 * @param conexion conexion activa
	 */
	public JugadorDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**
	 * busca un jugador utilizando el id registrado en la base de datos
	 * 
	 * @param id identificador del jugador
	 * @return jugador encontrado o null
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
	 * guarda un nuevo jugador dentro de la base de datos del sistema
	 * 
	 * @param jugador jugador a guardar
	 * @return true si se guardo correctamente
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
	 * actualiza el puntaje almacenado para un jugador especifico
	 * 
	 * @param jugador jugador actualizado
	 * @return true si el cambio fue exitoso
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
	 * verifica si ya existe un jugador registrado con el mismo nombre
	 * 
	 * @param nombre nombre a validar
	 * @return true si el nombre existe
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
