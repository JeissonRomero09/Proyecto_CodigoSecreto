package co.edu.poli.CodigoOculto.Controlador;

import java.sql.Connection;

import co.edu.poli.CodigoOculto.Dao.ConexionBD;
import co.edu.poli.CodigoOculto.Modelo.Jugador;
import co.edu.poli.CodigoOculto.Modelo.NumeroSecreto;
import co.edu.poli.CodigoOculto.Modelo.Partida;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

/**
 * controlador del menu principal del juego gestiona la navegacion y la sesion
 * del jugador
 */
public class MenuJuegoControlador {

	private Jugador jugador;

	@FXML
	private Text textoNombre;

	@FXML
	private Text textoId;

	private Connection conexion = ConexionBD.conectar();

	/**
	 * asigna el jugador actual y actualiza la informacion mostrada en pantalla
	 * 
	 * @param jugador jugador activo
	 */
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
		actualizarTexto();
	}

	/**
	 * inicializa el controlador al cargar la vista y establece la conexion con la
	 * base de datos
	 */
	@FXML
	public void initialize() {
		actualizarTexto();
		conexion = ConexionBD.conectar();
	}

	/**
	 * redirige a la pantalla de como jugar manteniendo la informacion del jugador
	 * 
	 * @param event evento generado por el boton
	 */
	@FXML
	public void irAComoJugar(ActionEvent event) {
		cambiarEscena(event, "/co/edu/poli/CodigoOculto/Vista/ComoJugar.fxml",
				(ComoJugarControlador controller) -> controller.setJugador(jugador));
	}

	/**
	 * inicia una nueva partida del juego y envia los datos necesarios al
	 * controlador
	 * 
	 * @param event evento generado por el boton
	 */
	@FXML
	public void irAPantallaJuego(ActionEvent event) {
		cambiarEscena(event, "/co/edu/poli/CodigoOculto/Vista/PantallaJuego.fxml",
				(PantallaJuegoControlador controller) -> {

					controller.setJugador(jugador);

					controller.setPartida(new Partida(jugador, new NumeroSecreto(5)));

					controller.setConexion(conexion);
				});
	}

	/**
	 * abre el modo de juego local uno contra uno
	 * 
	 * @param event evento generado por el boton
	 */
	@FXML
	public void irA1vs1Local(ActionEvent event) {
		cambiarEscena(event, "/co/edu/poli/CodigoOculto/Vista/_1vs1_Local.fxml",
				(_1vs1_LocalControlador controller) -> controller.setJugador(jugador));
	}

	/**
	 * abre la pantalla de historial del jugador y valida que no sea un usuario
	 * invitado
	 * 
	 * @param event evento generado por el boton
	 */
	@FXML
	public void irAHistorial(ActionEvent event) {

		if (jugador == null) {
			mostrarPopup("Error: no hay jugador cargado.");
			return;
		}

		if (jugador.esInvitado()) {
			mostrarPopup("No puedes ver el historial como invitado.");
			return;
		}

		try {

			if (conexion == null) {
				conexion = co.edu.poli.CodigoOculto.Dao.ConexionBD.conectar();
			}

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/Historial.fxml"));

			Parent root = loader.load();

			HistorialControlador controller = loader.getController();

			controller.setConexion(conexion);
			controller.setJugador(jugador);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			mostrarPopup("Error al abrir historial");
		}
	}

	/**
	 * cierra la sesion actual del jugador y regresa al menu principal
	 * 
	 * @param event evento generado por el boton
	 */
	@FXML
	public void cerrarSesion(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuPrincipal.fxml"));

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * cambia la escena actual y envia informacion al nuevo controlador
	 * 
	 * @param event    evento generado
	 * @param rutaFXML ruta de la vista fxml
	 * @param setter   metodo para enviar datos
	 */
	private <T> void cambiarEscena(ActionEvent event, String rutaFXML, ControllerSetter<T> setter) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
			Parent root = loader.load();

			T controller = loader.getController();

			if (setter != null) {
				setter.set(controller);
			}

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * interfaz utilizada para enviar datos al controlador de la nueva escena
	 */
	private interface ControllerSetter<T> {
		void set(T controller);
	}

	/**
	 * actualiza los textos del jugador mostrados dentro de la interfaz
	 */
	private void actualizarTexto() {
		if (jugador != null) {
			if (textoNombre != null) {
				textoNombre.setText("Name: " + jugador.getNombre());
			}
			if (textoId != null) {
				textoId.setText("ID: " + jugador.getId());
			}
		}
	}

	/**
	 * retorna el jugador actual de la sesion
	 * 
	 * @return jugador activo
	 */
	public Jugador getJugador() {
		return jugador;
	}

	/**
	 * muestra un mensaje emergente en pantalla durante unos segundos
	 * 
	 * @param mensaje texto a mostrar
	 */
	private void mostrarPopup(String mensaje) {

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label(mensaje);
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

		StackPane root = new StackPane(texto);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 30;");

		Scene scene = new Scene(root);
		popup.setScene(scene);

		popup.show();

		new Thread(() -> {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			javafx.application.Platform.runLater(popup::close);
		}).start();
	}
}
