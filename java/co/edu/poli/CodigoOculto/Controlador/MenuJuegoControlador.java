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
 * Controlador del menú principal del juego. Gestiona la navegación entre
 * pantallas y la sesión del jugador.
 */
public class MenuJuegoControlador {

	private Jugador jugador;

	@FXML
	private Text textoNombre;

	@FXML
	private Text textoId;

	private Connection conexion = ConexionBD.conectar();

	/**
	 * Asigna el jugador actual al menú y actualiza la vista.
	 */
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
		actualizarTexto();
	}

	/**
	 * Inicializa el controlador al cargar la vista.
	 */
	@FXML
	public void initialize() {
		actualizarTexto();
		conexion = ConexionBD.conectar();
	}

	/**
	 * Redirige a la pantalla "Cómo jugar".
	 */
	@FXML
	public void irAComoJugar(ActionEvent event) {
		cambiarEscena(event, "/co/edu/poli/CodigoOculto/Vista/ComoJugar.fxml",
				(ComoJugarControlador controller) -> controller.setJugador(jugador));
	}

	/**
	 * Inicia una partida individual contra la máquina o modo principal.
	 */
	@FXML
	public void irAPantallaJuego(ActionEvent event) {
		cambiarEscena(event, "/co/edu/poli/CodigoOculto/Vista/PantallaJuego.fxml",
				(PantallaJuegoControlador controller) -> {

					controller.setJugador(jugador);

					controller.setPartida(
							new Partida(
									jugador,
									new NumeroSecreto(5)
							)
					);

					controller.setConexion(conexion);
				});
	}

	/**
	 * Redirige al modo de juego 1 vs 1 local.
	 */
	@FXML
	public void irA1vs1Local(ActionEvent event) {
		cambiarEscena(event, "/co/edu/poli/CodigoOculto/Vista/_1vs1_Local.fxml",
				(_1vs1_LocalControlador controller) -> controller.setJugador(jugador));
	}

	/**
	 * Abre la pantalla de historial del jugador. Valida que el jugador no sea
	 * invitado y que exista conexión.
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
	 * Cierra la sesión del jugador y vuelve al menú principal del sistema.
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
	 * Método genérico para cambiar de escena y enviar datos al controlador destino.
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
	 * Interfaz funcional para inyectar datos al controlador destino.
	 */
	private interface ControllerSetter<T> {
		void set(T controller);
	}

	/**
	 * Actualiza la información del jugador en pantalla.
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
	 * Retorna el jugador actual.
	 */
	public Jugador getJugador() {
		return jugador;
	}

	/**
	 * Muestra un mensaje emergente tipo popup en pantalla.
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