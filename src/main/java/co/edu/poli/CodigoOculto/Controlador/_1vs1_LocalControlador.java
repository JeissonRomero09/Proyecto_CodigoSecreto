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
	 * Recibe el jugador 1 desde otra pantalla y actualiza la vista.
	 */
	public void setJugador(Jugador jugador) {
		this.jugador1 = jugador;
		actualizarTexto();
	}

	/**
	 * Método automático al cargar la vista JavaFX.
	 */
	@FXML
	public void initialize() {
		actualizarTexto();
	}

	/**
	 * Actualiza el texto en pantalla con el nombre del jugador 1.
	 */
	private void actualizarTexto() {
		if (textoNombre != null && jugador1 != null) {
			textoNombre.setText("Name J1: " + jugador1.getNombre());
		}
	}

	/**
	 * Inicia la partida 1 vs 1.
	 * 
	 * Validaciones: - ID válido - Conexión a base de datos - Existencia del jugador
	 * 2 - Reglas del modelo
	 * 
	 * Luego carga la pantalla de juego.
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
			 * Conexión a base de datos
			 */
			Connection conexion = ConexionBD.conectar();
			this.conexion = conexion;

			if (conexion == null) {
				mostrarPopup("Error de conexión con la base de datos");
				return;
			}

			/**
			 * Búsqueda del jugador 2
			 */
			JugadorDAO dao = new JugadorDAO(conexion);
			jugador2 = dao.buscarJugadorPorId(idInt);

			if (jugador2 == null) {
				mostrarPopup("No existe un jugador con el ID: " + idInt);
				return;
			}

			/**
			 * Creación del modelo de partida
			 */
			partida = new PartidaLocal_Vs();

			/**
			 * Validación de reglas del juego
			 */
			EstadoInicio estado = partida.validarInicio(jugador1, jugador2);

			if (estado == EstadoInicio.MISMO_JUGADOR) {
				mostrarPopup("No puedes jugar contra ti mismo");
				return;
			}

			/**
			 * Confirmación del usuario
			 */
			boolean confirmar = mostrarConfirmacion("¿Deseas iniciar partida 1 vs 1?\n\n"
					+ "Al terminar la partida, la sesión del jugador 2 se cerrará\n\n" + "ID: " + jugador2.getId()
					+ "\n" + "Nombre: " + jugador2.getNombre());

			if (!confirmar)
				return;

			partida.iniciar(jugador1, jugador2);

			/**
			 * Carga de pantalla de juego
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
	 * Muestra un popup informativo en pantalla.
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
	 * Muestra un popup de confirmación con botones.
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
	 * Regresa al menú principal del juego.
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