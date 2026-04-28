package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;

public class PantallaJuego1vs1Controlador {

	private PartidaLocal_Vs partidaVs;

	@FXML
	private GridPane gridCasillas;
	@FXML
	private GridPane gridCasillas1;
	@FXML
	private Text txtId;
	@FXML
	private Text txtId1;

	// Guardar jugadores para no perder sesión
	private Jugador jugador1;
	private Jugador jugador2;

	@FXML
	public void initialize() {
		Platform.runLater(this::configurarTeclado);
	}

	public void setJugadores(Jugador j1, Jugador j2) {

		this.jugador1 = j1;
		this.jugador2 = j2;

		partidaVs = new PartidaLocal_Vs();
		partidaVs.iniciar(j1, j2);

		txtId.setText("J1: " + j1.getNombre());
		txtId1.setText("J2: " + j2.getNombre());

		actualizarTurno();
	}

	private void configurarTeclado() {

		Scene scene = gridCasillas.getScene();
		if (scene == null)
			return;

		scene.setOnKeyPressed(e -> {

			if (partidaVs == null)
				return;
			if (!esTurnoActivo())
				return;

			Partida p = partidaVs.getPartidaActual();

			if (e.getCode().isDigitKey()) {

				String valor = e.getText();

				int f = p.getFilaActual();
				int c = p.getColumnaActual();

				if (p.realizarIntento(valor)) {
					Button b = getNodeActivo(f, c);
					if (b != null)
						b.setText(valor);
				}
			}

			if (e.getCode() == KeyCode.ENTER) {
				manejarEnter();
			}
		});
	}

	@FXML
	private void presionarBoton(ActionEvent event) {

		if (partidaVs == null)
			return;
		if (!esTurnoActivo())
			return;

		Button btn = (Button) event.getSource();
		String t = btn.getText();

		if (t.equalsIgnoreCase("Enter")) {
			manejarEnter();
			return;
		}

		Partida p = partidaVs.getPartidaActual();

		int f = p.getFilaActual();
		int c = p.getColumnaActual();

		if (p.realizarIntento(t)) {
			Button b = getNodeActivo(f, c);
			if (b != null)
				b.setText(t);
		}
	}

	private void manejarEnter() {

		Partida p = partidaVs.getPartidaActual();

		if (!p.esFilaCompleta())
			return;

		String estado = partidaVs.jugarTurno();

		if (estado.equals("FIN")) {
			System.out.println("Ganador: " + partidaVs.getGanador().getNombre());
			return;
		}

		if (estado.equals("RONDA_COMPLETA")) {

			Partida p1 = partidaVs.getPartidaJ1();
			Partida p2 = partidaVs.getPartidaJ2();

			pintarResultado(gridCasillas, p1.getFilaEvaluada(), p1.getUltimoResultado());
			pintarResultado(gridCasillas1, p2.getFilaEvaluada(), p2.getUltimoResultado());

			partidaVs.siguienteRonda();
		}

		actualizarTurno();
	}

	private void pintarResultado(GridPane grid, int fila, String[] resultado) {

		for (int i = 0; i < 5; i++) {

			Button b = getNode(grid, fila, i);
			if (b == null)
				continue;

			switch (resultado[i]) {
			case "VERDE":
				b.setStyle("-fx-background-color: #90EE90;");
				break;
			case "AMARILLO":
				b.setStyle("-fx-background-color: #FFD966;");
				break;
			default:
				b.setStyle("-fx-background-color: gray;");
			}
		}
	}

	@FXML
	private void seleccionarCasilla(MouseEvent event) {

		if (partidaVs == null)
			return;

		Button btn = (Button) event.getSource();
		Integer col = GridPane.getColumnIndex(btn);

		if (col != null && esTurnoActivo()) {
			partidaVs.getPartidaActual().moverCursorManual(col);
		}
	}

	private boolean esTurnoActivo() {

		return partidaVs.getTurnoActual() == partidaVs.getJugador1() ? !gridCasillas.isDisabled()
				: !gridCasillas1.isDisabled();
	}

	private void actualizarTurno() {

		boolean j1 = partidaVs.getTurnoActual() == partidaVs.getJugador1();

		gridCasillas.setDisable(!j1);
		gridCasillas1.setDisable(j1);
	}

	private Button getNodeActivo(int fila, int col) {

		GridPane grid = partidaVs.getTurnoActual() == partidaVs.getJugador1() ? gridCasillas : gridCasillas1;

		return getNode(grid, fila, col);
	}

	private Button getNode(GridPane grid, int fila, int col) {

		for (Node n : grid.getChildren()) {

			Integer f = GridPane.getRowIndex(n);
			Integer c = GridPane.getColumnIndex(n);

			if ((f == null ? 0 : f) == fila && (c == null ? 0 : c) == col) {
				return (Button) n;
			}
		}
		return null;
	}

	@FXML
	public void irMenuJugador(ActionEvent event) {

		try {

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

			Parent root = loader.load();

			// Obtener controlador del menú
			MenuJuegoControlador controller = loader.getController();

			// Enviar nuevamente el jugador logueado
			controller.setJugador(jugador1);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}