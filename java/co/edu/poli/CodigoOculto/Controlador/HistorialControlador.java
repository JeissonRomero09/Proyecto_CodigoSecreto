package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Dao.Partida_JugadorDAO;
import co.edu.poli.CodigoOculto.Modelo.Jugador;

import java.sql.Connection;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import co.edu.poli.CodigoOculto.Dao.ConexionBD;
import java.sql.Connection;

/**
 * controlador encargado de mostrar el historial del jugador
 * y sus estadisticas dentro del juego
 */
public class HistorialControlador {

	@FXML
	private Text textoNombre;
	@FXML
	private Text textoNombre1;
	@FXML
	private Text textoNombre11;
	@FXML
	private Text textoNombre111;
	@FXML
	private Text textoNombre112;
	@FXML
	private Text textoNombre1121;

	private Jugador jugador;
	private Partida_JugadorDAO partidaJugadorDAO;

	/**
	 * conexion utilizada para consultar la informacion
	 * almacenada en la base de datos
	 */
	private Connection conexion = ConexionBD.conectar();

	/**
	 * asigna la conexion y crea el dao de partidas
	 * para poder consultar las estadisticas del jugador
	 * @param conexion conexion activa a la base de datos
	 */
	public void setConexion(Connection conexion) {
		this.conexion = conexion;
		this.partidaJugadorDAO = new Partida_JugadorDAO(conexion);
		cargarSiListo();
	}

	/**
	 * asigna el jugador actual para cargar
	 * su historial y estadisticas
	 * @param jugador jugador activo
	 */
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
		cargarSiListo();
	}

	/**
	 * verifica que todos los datos necesarios
	 * esten listos antes de cargar la informacion
	 */
	private void cargarSiListo() {

		if (jugador == null)
			return;
		if (partidaJugadorDAO == null)
			return;
		if (textoNombre == null)
			return;

		cargarEstadisticas();
	}

	/**
	 * obtiene las estadisticas del jugador
	 * y las muestra en los textos de la vista
	 */
	private void cargarEstadisticas() {

		int id = jugador.getId();

		int jugadas = partidaJugadorDAO.contarPartidas(id);
		int ganadas = partidaJugadorDAO.contarVictorias(id);
		int perdidas = partidaJugadorDAO.contarDerrotas(id);
		double winrate = partidaJugadorDAO.calcularWinrate(id);

		textoNombre.setText("ID: " + id);
		textoNombre1.setText("Name: " + jugador.getNombre());

		textoNombre11.setText("Partidas jugadas: " + jugadas);
		textoNombre112.setText("Partidas ganadas: " + ganadas);
		textoNombre1121.setText("Partidas perdidas: " + perdidas);
		textoNombre111.setText("Winrate: " + String.format("%.2f", winrate) + "%");
	}

	/**
	 * regresa al menu principal del juego
	 * manteniendo la informacion del jugador
	 * @param event evento generado por el boton
	 */
	@FXML
	private void volverMenu(javafx.event.ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

			Parent root = loader.load();

			MenuJuegoControlador controller = loader.getController();
			controller.setJugador(jugador);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
