package co.edu.poli.CodigoOculto.Test.Integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.poli.CodigoOculto.Dao.ConexionBD;
import co.edu.poli.CodigoOculto.Dao.Partida_JugadorDAO;

public class testPartida_jugadorDAO {

    private Connection cn;
    private Partida_JugadorDAO dao;

    // ================================
    // 1. CONEXIÓN A BASE DE DATOS
    // ================================
    @BeforeEach
    public void setUp() {
        cn = ConexionBD.conectar();
        dao = new Partida_JugadorDAO(cn);
    }

    // ================================
    // 2. ESTADÍSTICAS DEL JUGADOR
    // ================================
    @Test
    public void testHistorialJugador() {

        int id = 1;

        int jugadas = dao.contarPartidas(id);
        int ganadas = dao.contarVictorias(id);
        int perdidas = dao.contarDerrotas(id);

        assertTrue(jugadas >= 0);
        assertTrue(ganadas >= 0);
        assertTrue(perdidas >= 0);

        double wr = dao.calcularWinrate(id);

        assertTrue(wr >= 0 && wr <= 100);
    }
}