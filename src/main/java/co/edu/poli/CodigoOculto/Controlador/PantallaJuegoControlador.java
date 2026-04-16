package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Partida;
import co.edu.poli.CodigoOculto.Modelo.Temporizador;
import co.edu.poli.CodigoOculto.Modelo.Jugador;

import java.util.Arrays;

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
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;

public class PantallaJuegoControlador {

	@FXML
	private Text txtId;
	@FXML
	private Text txtId1;
	@FXML
	private GridPane gridCasillas;

	private Partida partida = new Partida();
	private Temporizador temporizador = new Temporizador();
	private Timeline timeline;
	private Jugador jugador;

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
		if (txtId != null) {
			txtId.setText("Name: " + jugador.getNombre());
		}
	}

	@FXML
	public void initialize() {
		iniciarTemporizador();
		Platform.runLater(this::configurarTeclado);
	}

	// =========================
	// TECLADO
	// =========================
	private void configurarTeclado() {

		Scene scene = gridCasillas.getScene();
		if (scene == null)
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
					mostrarAlertaYReiniciar("¡GANASTE!");
					break;

				case "PERDISTE":
					mostrarAlertaYReiniciar("PERDISTE\nCombinación: " + Arrays.toString(partida.getCombinacion()));
					break;

				case "CONTINUA":
					iniciarTemporizador();
					break;
				}
			}
		});

		gridCasillas.requestFocus();
	}

	// =========================
	// TIMER (CORREGIDO MVC)
	// =========================
	private void iniciarTemporizador() {

		if (timeline != null) {
			timeline.stop();
		}

		temporizador.reiniciar();
		actualizarTiempo();

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

			temporizador.decrementar();
			actualizarTiempo();

			if (temporizador.tiempoAgotado()) {

				timeline.stop();

				// 🔥 SOLO MODELO DECIDE
				String estado = partida.tiempoAgotado();

				// 🔥 SOLO UI
				pintarFilaAnulada(partida.getFilaEvaluada());

				if (estado.equals("PERDISTE")) {
					mostrarAlertaYReiniciar("PERDISTE");
				} else {
					iniciarTemporizador();
				}
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	// =========================
	// UI: FILA ANULADA
	// =========================
	private void pintarFilaAnulada(int fila) {

		for (int i = 0; i < 5; i++) {

			Button casilla = getNode(fila, i);
			if (casilla == null)
				continue;

			casilla.setText("0");

			casilla.setStyle("-fx-background-color: gray;");
		}
	}

	// =========================
	// UI: RESULTADOS
	// =========================
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

	// =========================
	// ENTER BUTTON
	// =========================
	@FXML
	private void presionarBoton(ActionEvent event) {

		Button btn = (Button) event.getSource();
		String texto = btn.getText();

		if (texto.equalsIgnoreCase("Enter")) {

			String estado = partida.procesarIntento();

			pintarResultado(partida.getUltimoResultado());

			switch (estado) {

			case "GANASTE":
				mostrarAlertaYReiniciar("¡GANASTE!");
				break;

			case "PERDISTE":
				mostrarAlertaYReiniciar("PERDISTE\nCombinación: " + Arrays.toString(partida.getCombinacion()));
				break;

			case "CONTINUA":
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

	// =========================
	// CLICK CASILLA
	// =========================
	@FXML
	private void seleccionarCasilla(MouseEvent event) {

		Button casilla = (Button) event.getSource();

		Integer fila = GridPane.getRowIndex(casilla);
		Integer col = GridPane.getColumnIndex(casilla);

		if (fila != null && fila == partida.getFilaActual()) {
			partida.moverCursorManual(col == null ? 0 : col);
		}
	}

	// =========================
	// UTIL
	// =========================
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

	// =========================
	// ALERTA + RESET
	// =========================
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