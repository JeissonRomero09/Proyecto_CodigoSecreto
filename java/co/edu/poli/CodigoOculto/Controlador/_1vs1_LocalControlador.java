package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import co.edu.poli.CodigoOculto.Modelo.PartidaLocal_Vs;
import co.edu.poli.CodigoOculto.Modelo.PartidaLocal_Vs.EstadoInicio;
import co.edu.poli.CodigoOculto.Dao.ConexionBD;
import co.edu.poli.CodigoOculto.Dao.JugadorDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.sql.Connection;

/**
 * controlador encargado de gestionar
 * el inicio de partidas locales 1 vs 1
 */
public class _1vs1_LocalControlador {

	private Jugador jugador1;
	private Jugador jugador2;
	private PartidaLocal_Vs partida;
	private Connection conexion;

	@FXML
	private TextField txtId;

	@FXML
	private Text textoNombre;

	/**
	 * recibe el jugador principal desde
	 * otra pantalla y actualiza la vista
	 * @param jugador jugador principal
	 */
	public void setJugador(Jugador jugador) {
		this.jugador1 = jugador;
		actualizarTexto();
	}

	/**
	 * inicializa la pantalla cuando
	 * el fxml termina de cargar
	 */
	@FXML
	public void initialize() {
		actualizarTexto();
	}

	/**
	 * actualiza el texto mostrado con
	 * el nombre del jugador 1
	 */
	private void actualizarTexto() {
		if (textoNombre != null && jugador1 != null) {
			textoNombre.setText("Name J1: " + jugador1.getNombre());
		}
	}

	/**
	 * inicia una partida local entre dos jugadores
	 * realizando validaciones de ids y conexion
	 * @param event evento generado por el boton
	 */
	@FXML
	public void iniciarPartida(ActionEvent event) {

		try {

			String idTexto = txtId.getText();

			if (idTexto == null || idTexto.isBlank() || jugador1 == null) {
				mostrarPopup("Debes ingresar un ID válido");
				return;
			}

			int idInt;

			try {
				idInt = Integer.parseInt(idTexto.trim());
			} catch (NumberFormatException e) {
				mostrarPopup("Debes ingresar un número válido");
				return;
			}

			/**
			 * conexion a base de datos
			 */
			Connection conexion = ConexionBD.conectar();
			this.conexion = conexion;

			if (conexion == null) {
				mostrarPopup("Error de conexión con la base de datos");
				return;
			}

			/**
			 * busqueda del jugador 2
			 */
			JugadorDAO dao = new JugadorDAO(conexion);
			jugador2 = dao.buscarJugadorPorId(idInt);

			if (jugador2 == null) {
				mostrarPopup("No existe un jugador con el ID: " + idInt);
				return;
			}

			/**
			 * creacion del modelo de partida
			 */
			partida = new PartidaLocal_Vs();

			/**
			 * validacion de reglas del juego
			 */
			EstadoInicio estado = partida.validarInicio(jugador1, jugador2);

			if (estado == EstadoInicio.MISMO_JUGADOR) {
				mostrarPopup("No puedes jugar contra ti mismo");
				return;
			}

			/**
			 * confirmacion del usuario antes
			 * de comenzar la partida
			 */
			boolean confirmar = mostrarConfirmacion(
					"¿Deseas iniciar partida 1 vs 1?\n\n"
					+ "Al terminar la partida, la sesión del jugador 2 se cerrará\n\n"
					+ "ID: " + jugador2.getId()
					+ "\n"
					+ "Nombre: " + jugador2.getNombre());

			if (!confirmar)
				return;

			partida.iniciar(jugador1, jugador2);

			/**
			 * carga de pantalla de juego
			 */
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/PantallaJuego1vs1.fxml"));

			Parent root = loader.load();

			PantallaJuego1vs1Controlador controller = loader.getController();

			controller.setJugadores(jugador1, jugador2);
			controller.setPartida(partida);
			controller.setConexion(conexion);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.centerOnScreen();
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			mostrarPopup("Error inesperado: " + e.getMessage());
		}
	}

	/**
	 * muestra un mensaje emergente temporal
	 * con informacion o errores del sistema
	 * @param mensaje mensaje mostrado
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
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			javafx.application.Platform.runLater(popup::close);
		}).start();
	}

	/**
	 * muestra una ventana de confirmacion
	 * antes de iniciar la partida
	 * @param mensaje mensaje de confirmacion
	 * @return true si acepta iniciar
	 */
	private boolean mostrarConfirmacion(String mensaje) {

		final boolean[] respuesta = { false };

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label(mensaje);
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
		texto.setWrapText(true);

		Button aceptar = new Button("Iniciar partida");
		Button cancelar = new Button("No soy yo");

		aceptar.setOnAction(e -> {
			respuesta[0] = true;
			popup.close();
		});

		cancelar.setOnAction(e -> {
			respuesta[0] = false;
			popup.close();
		});

		VBox botones = new VBox(10, aceptar, cancelar);
		botones.setAlignment(Pos.CENTER);

		VBox contenido = new VBox(20, texto, botones);
		contenido.setAlignment(Pos.CENTER);
		contenido.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 30;");

		Scene scene = new Scene(contenido);
		popup.setScene(scene);

		popup.showAndWait();

		return respuesta[0];
	}

	/**
	 * regresa al menu principal del juego
	 * manteniendo la sesion del jugador 1
	 * @param event evento generado por el boton
	 */
	@FXML
	public void volverMenu(ActionEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

			Parent root = loader.load();

			MenuJuegoControlador controller = loader.getController();
			controller.setJugador(jugador1);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
