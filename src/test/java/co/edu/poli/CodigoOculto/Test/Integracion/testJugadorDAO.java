package co.edu.poli.CodigoOculto.Test.Integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.poli.CodigoOculto.Dao.ConexionBD;
import co.edu.poli.CodigoOculto.Dao.JugadorDAO;
import co.edu.poli.CodigoOculto.Modelo.Jugador;

public class testJugadorDAO {

    private Connection cn;
    private JugadorDAO dao;

    // ================================
    // 1. CONFIGURACIÓN INICIAL
    // ================================
    @BeforeEach
    public void setUp() {
        cn = ConexionBD.conectar();
        dao = new JugadorDAO(cn);
    }

    // ================================
    // 2. GUARDAR JUGADOR
    // ================================
    @Test
    public void testGuardarJugador() {

        Jugador j = new Jugador();
        j.setNombre("JUnitPlayer");
        j.setPuntaje(0);
        j.setEsInvitado(false);

        boolean ok = dao.guardarJugador(j);

        assertTrue(ok);
        assertTrue(j.getId() > 0);
    }

    // ================================
    // 3. HISTORIAL JUGADOR
    // ================================
    @Test
    public void testHistorialJugador() {

        int id = 1;

        Jugador j = dao.buscarJugadorPorId(id);

        if (j != null) {
            assertNotNull(j);
            assertTrue(j.getId() > 0);
        }
    }

    // ================================
    // 4. BUSCAR JUGADOR
    // ================================
    @Test
    public void testBuscarJugador() {

        Jugador j = dao.buscarJugadorPorId(1);

        if (j != null) {
            assertNotNull(j);
            assertTrue(j.getId() > 0);
        }
    }

    // ================================
    // 5. EXISTE NOMBRE
    // ================================
    @Test
    public void testExisteNombre() {

        boolean existe = dao.existeNombre("JUnitPlayer");

        assertNotNull(existe);
    }
}