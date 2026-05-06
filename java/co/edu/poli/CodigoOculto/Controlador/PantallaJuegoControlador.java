package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Partida;
import co.edu.poli.CodigoOculto.Modelo.Temporizador;
import co.edu.poli.CodigoOculto.Modelo.Jugador;
import co.edu.poli.CodigoOculto.Dao.PartidaDAO;
import co.edu.poli.CodigoOculto.Dao.Partida_JugadorDAO;

import java.util.Arrays;
import java.util.Optional;
import java.sql.Connection;

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class PantallaJuegoControlador {

	private Partida partida;
	private Temporizador temporizador;
	private Timeline timeline;
	private Jugador jugador;

	
	private PartidaDAO partidaDAO;
	private Partida_JugadorDAO partidaJugadorDAO;

	@FXML
	private Text txtId;

	@FXML
	private Text txtId1;

	@FXML
	private GridPane gridCasillas;

	// recibir partida
	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	// recibir jugador
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
		if (txtId != null) {
			txtId.setText("Name: " + jugador.getNombre());
		}
	}

	public void setConexion(Connection conexion) {
		this.partidaDAO = new PartidaDAO(conexion);
		this.partidaJugadorDAO = new Partida_JugadorDAO(conexion);
	}

	@FXML
	public void initialize() {
		temporizador = new Temporizador();
		iniciarTemporizador();

		Platform.runLater(() -> {
			configurarTeclado();
			gridCasillas.requestFocus();
		});
	}

	private void guardarHistorial(String resultado) {

		int idPartida = partidaDAO.crearPartida();

		if (idPartida == -1) {
			System.out.println("Error creando partida");
			return;
		}

		partidaJugadorDAO.guardar(idPartida, jugador.getId(), resultado);
	}

	// volver al menu
	@FXML
	private void irMenuJugador(ActionEvent event) {

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label("¿Deseas terminar la partida?");
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

		Button btnSalir = new Button("Salir");
		Button btnCancelar = new Button("Cancelar");

		btnSalir.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
		btnCancelar.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");

		StackPane root = new StackPane(texto);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 30;");

		// agregar botones al layout
		root.getChildren().addAll(btnSalir, btnCancelar);

		StackPane.setAlignment(btnSalir, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(btnCancelar, Pos.BOTTOM_RIGHT);

		Scene scene = new Scene(root, 400, 200);
		popup.setScene(scene);
		popup.show();

		// ACCIÓN BOTÓN SALIR
		btnSalir.setOnAction(e -> {

			try {
				if (timeline != null) {
					timeline.stop();
				}

				popup.close();

				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

				Parent rootMenu = loader.load();

				MenuJuegoControlador controller = loader.getController();
				controller.setJugador(jugador);

				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(new Scene(rootMenu));
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		// ACCIÓN CANCELAR
		btnCancelar.setOnAction(e -> popup.close());
	}

	// configurar teclado
	private void configurarTeclado() {

		Scene scene = gridCasillas.getScene();
		if (scene == null || partida == null)
			return;

		scene.setOnKeyPressed(event -> {

			KeyCode code = event.getCode();

			if (code.isDigitKey()) {

				String valor = event.getText();

				int fila = partida.getFilaActual();
				int col = partida.getColumnaActual();

				if (partida.realizarIntento(valor)) {

					Button casilla = getNode(fila, col);
					if (casilla != null) {
						casilla.setText(valor);
					}
				}
			}

			if (code == KeyCode.ENTER) {

				String estado = partida.procesarIntento();

				pintarResultado(partida.getUltimoResultado());

				switch (estado) {

				case "GANASTE":
					guardarHistorial("VICTORIA"); 
					mostrarAlertaYReiniciar("GANASTE");
					break;

				case "PERDISTE":
					guardarHistorial("DERROTA"); 
					mostrarAlertaYReiniciar("PERDISTE\nCombinacion: " + Arrays.toString(partida.getCombinacion()));
					break;

				case "CONTINUA":
					iniciarTemporizador();
					break;
				}
			}
		});

		gridCasillas.requestFocus();
	}

	// iniciar temporizador
	private void iniciarTemporizador() {

		if (timeline != null) {
			timeline.stop();
		}

		temporizador.reiniciar();
		actualizarTiempo();

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

			temporizador.decrementar();
			actualizarTiempo();

			if (temporizador.tiempoAgotado() && partida != null) {

				timeline.stop();

				String estado = partida.tiempoAgotado();

				pintarFilaAnulada(partida.getFilaEvaluada());

				if (estado.equals("PERDISTE")) {

					guardarHistorial("DERROTA"); 
					mostrarAlertaYReiniciar("PERDISTE");

				} else {
					iniciarTemporizador();
				}
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	// pintar fila anulada
	private void pintarFilaAnulada(int fila) {

		for (int i = 0; i < 5; i++) {

			Button casilla = getNode(fila, i);
			if (casilla == null)
				continue;

			casilla.setText("0");
			casilla.setStyle("-fx-background-color: gray;");
		}
	}

	// pintar resultado
	private void pintarResultado(String[] resultado) {

		int fila = partida.getFilaEvaluada();

		for (int i = 0; i < 5; i++) {

			Button casilla = getNode(fila, i);
			if (casilla == null)
				continue;

			switch (resultado[i]) {

			case "VERDE":
				casilla.setStyle("-fx-background-color: #90EE90;");
				break;

			case "AMARILLO":
				casilla.setStyle("-fx-background-color: #FFD966;");
				break;

			default:
				casilla.setStyle("-fx-background-color: gray;");
			}
		}
	}

	@FXML
	private void presionarBoton(ActionEvent event) {

	    if (partida == null)
	        return;

	    Button btn = (Button) event.getSource();
	    String texto = btn.getText();

	    if (texto.equalsIgnoreCase("Enter")) {

	    	if (!partida.esFilaCompleta()) {
	    		mostrarAlertaTemporal("Completa toda la fila antes de continuar");
	    		return;
	    	}

	    	/**
	    	 * Valida que la fila no contenga ceros
	    	 */
	    	for (int i = 0; i < 5; i++) {

	    		if (partida.getValorCelda(partida.getFilaActual(), i).equals("0")) {

	    			mostrarAlertaTemporal("No puedes ingresar 0");
	    			return;
	    		}
	    	}

	    	String estado = partida.procesarIntento();

	        switch (estado) {

	        case "GANASTE":
	            pintarResultado(partida.getUltimoResultado());
	            guardarHistorial("VICTORIA"); 
	            mostrarAlertaYReiniciar("GANASTE");
	            break;

	        case "PERDISTE":
	            pintarResultado(partida.getUltimoResultado());
	            guardarHistorial("DERROTA"); 
	            mostrarAlertaYReiniciar("PERDISTE\nCombinacion: " 
	                + Arrays.toString(partida.getCombinacion()));
	            break;

	        case "CONTINUA":
	            pintarResultado(partida.getUltimoResultado());
	            iniciarTemporizador();
	            break;
	        }
	        return;
	    }
	    
	    

	    int fila = partida.getFilaActual();
	    int col = partida.getColumnaActual();

	    if (partida.realizarIntento(texto)) {

	        Button casilla = getNode(fila, col);
	        if (casilla != null) {
	            casilla.setText(texto);
	        }
	    }
	}

	@FXML
	private void seleccionarCasilla(MouseEvent event) {

		if (partida == null)
			return;

		Button casilla = (Button) event.getSource();

		Integer fila = GridPane.getRowIndex(casilla);
		Integer col = GridPane.getColumnIndex(casilla);

		if (fila != null && fila == partida.getFilaActual()) {
			partida.moverCursorManual(col == null ? 0 : col);
		}
	}

	private void actualizarTiempo() {
		txtId1.setText(temporizador.getTiempoFormateado());
	}

	private Button getNode(int fila, int columna) {

		for (Node node : gridCasillas.getChildren()) {

			Integer f = GridPane.getRowIndex(node);
			Integer c = GridPane.getColumnIndex(node);

			if ((f == null ? 0 : f) == fila && (c == null ? 0 : c) == columna) {
				return (Button) node;
			}
		}
		return null;
	}
	
	private void mostrarAlertaTemporal(String mensaje) {

	    Stage popup = new Stage();
	    popup.initStyle(StageStyle.UNDECORATED);

	    Label texto = new Label(mensaje);
	    texto.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

	    StackPane root = new StackPane(texto);
	    root.setAlignment(Pos.CENTER);
	    root.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 25;");

	    popup.setScene(new Scene(root));
	    popup.show();

	    PauseTransition pausa = new PauseTransition(Duration.seconds(1));

	    pausa.setOnFinished(e -> popup.close());

	    pausa.play();
	}

	private void mostrarAlertaYReiniciar(String mensaje) {

		if (timeline != null) {
			timeline.stop();
		}

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

		PauseTransition pausa = new PauseTransition(Duration.seconds(5));

		pausa.setOnFinished(e -> {
			try {
				popup.close();

				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

				Parent rootMenu = loader.load();

				MenuJuegoControlador controller = loader.getController();
				controller.setJugador(jugador);

				Stage stage = (Stage) gridCasillas.getScene().getWindow();
				stage.setScene(new Scene(rootMenu));
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		pausa.play();
	}
}