/**
 * DAO encargado de la creación de partidas.
 */
package co.edu.poli.CodigoOculto.Dao;

import java.sql.*;

public class PartidaDAO {

	private Connection conexion;

	public PartidaDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**
	 * Crea una nueva partida y retorna su ID generado.
	 */
	public int crearPartida() {

		String sql = "INSERT INTO partida (id) VALUES (NULL)";

		try {

			PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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