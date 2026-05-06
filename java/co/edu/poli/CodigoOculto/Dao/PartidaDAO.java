package co.edu.poli.CodigoOculto.Dao;

import java.sql.*;

/**
 * dao encargado de gestionar
 * la creacion de partidas en la bd
 */
public class PartidaDAO {

	private Connection conexion;

	/**
	 * recibe la conexion utilizada
	 * para ejecutar consultas sql
	 * @param conexion conexion activa
	 */
	public PartidaDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**
	 * crea una nueva partida dentro
	 * de la base de datos y retorna
	 * el id generado automaticamente
	 * @return id de la partida creada
	 */
	public int crearPartida() {

		String sql = "INSERT INTO partida (id) VALUES (NULL)";

		try {

			PreparedStatement ps = conexion.prepareStatement(
					sql,
					Statement.RETURN_GENERATED_KEYS);

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				int id = rs.getInt(1);
				return id;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}
}
