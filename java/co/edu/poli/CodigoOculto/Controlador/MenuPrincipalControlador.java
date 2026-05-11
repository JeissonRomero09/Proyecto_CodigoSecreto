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
 * controlador del menu principal del sistema permite iniciar sesion registrar
 * jugadores y acceder como invitado
 */
public class MenuPrincipalControlador {

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNombre;

	private JugadorDAO jugadorDAO;

	/**
	 * inicializa la conexion con la base de datos y crea el dao de jugadores
	 */
	@FXML
	public void initialize() {
		jugadorDAO = new JugadorDAO(ConexionBD.conectar());
	}

	/**
	 * permite iniciar sesion usando el id del jugador valida la informacion
	 * ingresada antes de acceder al menu principal del juego
	 * 
	 * @param event evento generado por el boton
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
	 * registra un nuevo jugador en el sistema verificando que el nombre no exista
	 * previamente en la base de datos
	 * 
	 * @param event evento generado por el boton
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
	 * redirige automaticamente al menu del juego despues de registrar un jugador
	 * nuevo
	 * 
	 * @param jugador jugador registrado
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
	 * permite ingresar al juego como invitado sin necesidad de iniciar sesion
	 * 
	 * @param event evento generado por el boton
	 */
	@FXML
	public void irAMenuJuego(ActionEvent event) {
		irMenuJuego(event, Jugador.crearInvitado());
	}

	/**
	 * cambia la escena actual al menu del juego y envia la informacion del jugador
	 * 
	 * @param event   evento generado
	 * @param jugador jugador activo
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
	 * muestra un popup temporal con un mensaje (popup: ventana emergente) visible
	 * para el usuario
	 * 
	 * @param mensaje texto mostrado en pantalla
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
