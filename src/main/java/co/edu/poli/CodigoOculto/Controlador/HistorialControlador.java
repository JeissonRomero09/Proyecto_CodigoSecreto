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
 * Controlador encargado de mostrar el historial y estadísticas del jugador.
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
	 * Conexión a base de datos utilizada para consultar estadísticas.
	 */
	private Connection conexion = ConexionBD.conectar();

	/**
	 * Asigna la conexión a la base de datos y crea el DAO de partidas.
	 */
	public void setConexion(Connection conexion) {
		this.conexion = conexion;
		this.partidaJugadorDAO = new Partida_JugadorDAO(conexion);
		cargarSiListo();
	}

	/**
	 * Asigna el jugador actual para mostrar su historial.
	 */
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
		cargarSiListo();
	}

	/**
	 * Verifica que todos los datos necesarios estén listos antes de cargar
	 * información.
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
	 * Consulta la base de datos y carga las estadísticas del jugador.
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
	 * Regresa al menú principal del juego.
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