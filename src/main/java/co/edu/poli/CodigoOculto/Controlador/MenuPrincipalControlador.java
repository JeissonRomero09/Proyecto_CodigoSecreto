package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import co.edu.poli.CodigoOculto.Dao.JugadorDAO;
import co.edu.poli.CodigoOculto.Dao.ConexionBD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controlador del menú principal del sistema. Permite registro de jugadores,
 * inicio de sesión y acceso como invitado.
 */
public class MenuPrincipalControlador {

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNombre;

	private JugadorDAO jugadorDAO;

	/**
	 * Inicializa la conexión con la base de datos y el DAO de jugadores.
	 */
	@FXML
	public void initialize() {
		jugadorDAO = new JugadorDAO(ConexionBD.conectar());
	}

	/**
	 * Permite iniciar sesión de un jugador mediante su ID. Valida entrada, verifica
	 * existencia y redirige al menú del juego.
	 */
	@FXML
	public void iniciarSesion(ActionEvent event) {

		String textoId = txtId.getText();

		if (textoId == null || textoId.trim().isEmpty()) {
			mostrarPopup("Ingrese un ID");
			return;
		}

		int id;

		try {
			id = Integer.parseInt(textoId.trim());
		} catch (NumberFormatException e) {
			mostrarPopup("El ID debe ser numérico");
			return;
		}

		Jugador jugador = jugadorDAO.buscarJugadorPorId(id);

		if (jugador == null) {
			mostrarPopup("ID no encontrado");
			return;
		}

		irMenuJuego(event, jugador);
	}

	/**
	 * Registra un nuevo jugador en el sistema validando su nombre. Evita duplicados
	 * y confirma el registro antes de guardarlo.
	 */
	@FXML
	public void registrarJugador(ActionEvent event) {

		String nombre = txtNombre.getText();

		if (nombre == null || nombre.trim().isEmpty()) {
			mostrarPopup("Error\nIngrese un nombre");
			return;
		}

		if (jugadorDAO.existeNombre(nombre)) {
			mostrarPopup("Error\nEse nombre ya está registrado");
			return;
		}

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label("¿Registrar el nombre: " + nombre + "?");
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

		Button aceptar = new Button("Agregar Name");
		Button cancelar = new Button("Cambiar Name");

		aceptar.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
		cancelar.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

		StackPane root = new StackPane();

		StackPane layout = new StackPane(texto);
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setStyle("-fx-padding: 20;");

		StackPane botones = new StackPane();
		botones.getChildren().addAll(aceptar, cancelar);

		StackPane.setAlignment(aceptar, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(cancelar, Pos.BOTTOM_RIGHT);

		root.getChildren().addAll(layout, botones);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 30;");

		popup.setScene(new Scene(root, 420, 200));
		popup.show();

		cancelar.setOnAction(e -> popup.close());

		aceptar.setOnAction(e -> {

			popup.close();

			Jugador nuevo = new Jugador(nombre);
			jugadorDAO.guardarJugador(nuevo);

			mostrarPopup("Registro exitoso\n" + "Tu ID es: " + nuevo.getId() + "\n" + "Guárdalo para iniciar sesión");

			PauseTransition delay = new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(ev -> irMenuJuegoDesdePopup(nuevo));
			delay.play();
		});
	}

	/**
	 * Redirige al menú del juego después de un registro exitoso.
	 */
	private void irMenuJuegoDesdePopup(Jugador jugador) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

			Parent root = loader.load();

			MenuJuegoControlador controller = loader.getController();
			controller.setJugador(jugador);

			Stage stage = (Stage) txtNombre.getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.centerOnScreen();
			stage.toFront();
			stage.requestFocus();
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permite ingresar al sistema como jugador invitado.
	 */
	@FXML
	public void irAMenuJuego(ActionEvent event) {
		irMenuJuego(event, Jugador.crearInvitado());
	}

	/**
	 * Cambia la escena al menú del juego con el jugador indicado.
	 */
	private void irMenuJuego(ActionEvent event, Jugador jugador) {

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

	/**
	 * Muestra un popup de alerta temporal (versión estándar del sistema).
	 */
	private void mostrarAlerta(String titulo, String mensaje) {

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label(mensaje);
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

		StackPane root = new StackPane(texto);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 30;");

		Scene scene = new Scene(root);
		popup.setScene(scene);

		PauseTransition pausa = new PauseTransition(Duration.seconds(2));
		pausa.setOnFinished(e -> popup.close());
		pausa.play();
	}

	/**
	 * Muestra un popup temporal con mensaje en pantalla.
	 */
	private void mostrarPopup(String mensaje) {

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label(mensaje);
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

		StackPane root = new StackPane(texto);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 30;");

		Scene scene = new Scene(root, 400, 200);
		popup.setScene(scene);
		popup.show();

		PauseTransition pausa = new PauseTransition(Duration.seconds(3));
		pausa.setOnFinished(e -> popup.close());
		pausa.play();
	}
}