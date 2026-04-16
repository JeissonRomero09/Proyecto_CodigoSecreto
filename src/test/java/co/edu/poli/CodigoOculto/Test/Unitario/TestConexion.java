package co.edu.poli.CodigoOculto.Test.Unitario;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import co.edu.poli.CodigoOculto.Dao.ConexionBD;

public class TestConexion {

	// Verifica que la conexión no sea null
	@Test
	public void testConexionNoNula() {

		Connection cn = ConexionBD.conectar();

		assertNotNull(cn, " La conexión a la base de datos es null");

	}

	// Verifica que la conexión esté abierta
	@Test
	public void testConexionAbierta() throws Exception {

		Connection cn = ConexionBD.conectar();

		assertFalse(cn.isClosed(), " La conexión está cerrada");

		cn.close();
	}

	// Verifica que se puede cerrar la conexión
	@Test
	public void testCerrarConexion() throws Exception {

		Connection cn = ConexionBD.conectar();

		cn.close();

		assertTrue(cn.isClosed(), " La conexión no se cerró correctamente");
	}

	// Verifica múltiples conexiones
	@Test
	public void testMultiplesConexiones() {

		Connection cn1 = ConexionBD.conectar();
		Connection cn2 = ConexionBD.conectar();

		assertNotNull(cn1);
		assertNotNull(cn2);

		assertNotSame(cn1, cn2, " Las conexiones no deberían ser la misma instancia");
	}

}