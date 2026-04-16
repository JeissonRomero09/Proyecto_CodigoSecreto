package co.edu.poli.CodigoOculto.Modelo;

public class Jugador {

    private int id;
    private String nombre;
    private int puntaje;
    private boolean esInvitado;

    // Constructor vacío
    public Jugador() {}

    // Desde BD
    public Jugador(int id, String nombre, int puntaje, boolean esInvitado) {
        this.id = id;
        this.nombre = nombre;
        this.puntaje = puntaje;
        this.esInvitado = esInvitado;
    }

    // Crear jugador nuevo
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.esInvitado = false;
    }

    // Invitado
    public static Jugador crearInvitado() {
        Jugador j = new Jugador();
        j.nombre = "INVITADO";
        j.esInvitado = true;
        j.puntaje = 0;
        j.id = 0;
        return j;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }

    public boolean isEsInvitado() { return esInvitado; }
    public void setEsInvitado(boolean esInvitado) { this.esInvitado = esInvitado; }
}