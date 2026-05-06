package co.edu.poli.CodigoOculto.Dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * clase encargada de administrar
 * la conexion con la base de datos
 */
public class ConexionBD {

	private static final String URL = "jdbc:mysql://localhost:3306/CodigoSecreto";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	/**
	 * establece y retorna una conexion
	 * activa con la base de datos mysql
	 * utilizada por el sistema
	 * @return conexion activa a la bd
	 */
	public static Connection conectar() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (Exception e) {
			throw new RuntimeException("Error de conexión a la BD", e);
		}
	}
}
