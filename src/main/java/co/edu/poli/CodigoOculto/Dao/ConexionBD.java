/**
 * Clase encargada de la conexión a la base de datos.
 */
package co.edu.poli.CodigoOculto.Dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

	private static final String URL = "jdbc:mysql://localhost:3306/CodigoSecreto";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	/**
	 * Establece la conexión con la base de datos.
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