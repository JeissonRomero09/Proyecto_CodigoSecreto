package co.edu.poli.CodigoOculto.Modelo;

import java.util.Random;

public class PartidaLocal_Vs {

    private Jugador jugador1;
    private Jugador jugador2;

    private Partida partidaJ1;
    private Partida partidaJ2;

    private Jugador turnoActual;

    private boolean j1YaJugo;
    private boolean j2YaJugo;

    private Jugador ganador;

    // iniciar partida
    public void iniciar(Jugador j1, Jugador j2) {

        this.jugador1 = j1;
        this.jugador2 = j2;

        this.partidaJ1 = new Partida();
        this.partidaJ2 = new Partida();

        Random r = new Random();
        this.turnoActual = (r.nextBoolean()) ? jugador1 : jugador2;

        this.j1YaJugo = false;
        this.j2YaJugo = false;
        this.ganador = null;
    }

    // partida del turno actual
    public Partida getPartidaActual() {
        return (turnoActual == jugador1) ? partidaJ1 : partidaJ2;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    // ejecutar intento
    public String jugarTurno() {

        String resultado = getPartidaActual().procesarIntento();

        if (turnoActual == jugador1) {
            j1YaJugo = true;
        } else {
            j2YaJugo = true;
        }

        // victoria inmediata
        if (resultado.equals("GANASTE")) {
            ganador = turnoActual;
            return "FIN";
        }

        // ambos jugaron
        if (j1YaJugo && j2YaJugo) {
            return "RONDA_COMPLETA";
        }

        // cambiar turno
        cambiarTurno();
        return "SIGUE";
    }

    private void cambiarTurno() {
        turnoActual = (turnoActual == jugador1) ? jugador2 : jugador1;
    }

    // nueva ronda
    public void siguienteRonda() {
        j1YaJugo = false;
        j2YaJugo = false;
        cambiarTurno();
    }

    // getters necesarios
    public Partida getPartidaJ1() {
        return partidaJ1;
    }

    public Partida getPartidaJ2() {
        return partidaJ2;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }
}