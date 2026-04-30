package co.edu.poli.CodigoOculto.Test.Unitario;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import co.edu.poli.CodigoOculto.Dao.ConexionBD;

public class TestConexion {

    // ================================
    // 1. CONEXIÓN NO NULA
    // ================================
    @Test
    public void testConexionNoNula() {

        Connection cn = ConexionBD.conectar();

        assertNotNull(cn, "La conexión es null");
    }

    // ================================
    // 2. CONEXIÓN ABIERTA
    // ================================
    @Test
    public void testConexionAbierta() throws Exception {

        Connection cn = ConexionBD.conectar();

        assertNotNull(cn);
        assertFalse(cn.isClosed(), "La conexión está cerrada");

        cn.close();
    }

    // ================================
    // 3. CERRAR CONEXIÓN
    // ================================
    @Test
    public void testCerrarConexion() throws Exception {

        Connection cn = ConexionBD.conectar();

        cn.close();

        assertTrue(cn.isClosed(), "No se cerró correctamente");
    }

    // ================================
    // 4. VALIDACIÓN DE SQL REAL
    // ================================
    @Test
    public void testConexionPermiteQuery() throws Exception {

        Connection cn = ConexionBD.conectar();

        assertDoesNotThrow(() -> {
            cn.prepareStatement("SELECT 1").executeQuery();
        });

        cn.close();
    }
}