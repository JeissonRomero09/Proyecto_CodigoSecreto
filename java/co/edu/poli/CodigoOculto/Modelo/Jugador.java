package co.edu.poli.CodigoOculto.Modelo;

/**
 * clase que representa a un jugador dentro del sistema del juego
 */
public class Jugador {

	private int id;
	private String nombre;
	private int puntaje;
	private boolean esInvitado;

	/**
	 * constructor vacio utilizado para crear objetos sin datos iniciales del
	 * jugador
	 */
	public Jugador() {
	}

	/**
	 * constructor utilizado para cargar datos provenientes de la base de datos
	 * 
	 * @param id         identificador del jugador
	 * @param nombre     nombre del jugador
	 * @param puntaje    puntaje acumulado
	 * @param esInvitado indica si es invitado
	 */
	public Jugador(int id, String nombre, int puntaje, boolean esInvitado) {
		this.id = id;
		this.nombre = nombre;
		this.puntaje = puntaje;
		this.esInvitado = esInvitado;
	}

	/**
	 * verifica si el jugador actual corresponde a un invitado dentro del sistema
	 * 
	 * @return true si es invitado
	 */
	public boolean esInvitado() {
		return this.id == 0;
	}

	/**
	 * constructor utilizado para crear un nuevo jugador registrado en el sistema
	 * 
	 * @param nombre nombre del jugador
	 */
	public Jugador(String nombre) {
		this.nombre = nombre;
		this.puntaje = 0;
		this.esInvitado = false;
	}

	/**
	 * crea y retorna un jugador temporal de tipo invitado para jugar sin registro
	 * 
	 * @return jugador invitado
	 */
	public static Jugador crearInvitado() {

		Jugador j = new Jugador();

		j.nombre = "INVITADO";
		j.esInvitado = true;
		j.puntaje = 0;
		j.id = 0;

		return j;
	}

	/**
	 * retorna el id del jugador
	 * 
	 * @return id del jugador
	 */
	public int getId() {
		return id;
	}

	/**
	 * asigna el id del jugador
	 * 
	 * @param id nuevo id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * retorna el nombre del jugador
	 * 
	 * @return nombre del jugador
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * asigna el nombre del jugador
	 * 
	 * @param nombre nuevo nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * retorna el puntaje acumulado del jugador
	 * 
	 * @return puntaje del jugador
	 */
	public int getPuntaje() {
		return puntaje;
	}

	/**
	 * asigna un nuevo puntaje al jugador actual
	 * 
	 * @param puntaje nuevo puntaje
	 */
	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}

	/**
	 * indica si el jugador fue creado como invitado
	 * 
	 * @return true si es invitado
	 */
	public boolean isEsInvitado() {
		return esInvitado;
	}

	/**
	 * cambia el estado de invitado del jugador actual
	 * 
	 * @param esInvitado nuevo estado
	 */
	public void setEsInvitado(boolean esInvitado) {
		this.esInvitado = esInvitado;
	}
}
