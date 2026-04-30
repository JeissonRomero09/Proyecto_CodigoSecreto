package co.edu.poli.CodigoOculto.Test.Integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.poli.CodigoOculto.Dao.ConexionBD;
import co.edu.poli.CodigoOculto.Dao.PartidaDAO;

public class testPartidaDAO {

    private Connection cn;
    private PartidaDAO dao;

    // ================================
    // 1. CONEXIÓN A BASE DE DATOS
    // ================================
    @BeforeEach
    public void setUp() {
        cn = ConexionBD.conectar();
        dao = new PartidaDAO(cn);
    }

    // ================================
    // 2. CREAR PARTIDA
    // ================================
    @Test
    public void testCrearPartida() {

        int id = dao.crearPartida();

        assertTrue(id > 0, "No se generó ID de partida");
    }
}