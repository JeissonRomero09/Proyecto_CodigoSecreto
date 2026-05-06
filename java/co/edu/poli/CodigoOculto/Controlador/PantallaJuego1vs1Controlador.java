package co.edu.poli.CodigoOculto.Controlador;

import java.sql.Connection;

import co.edu.poli.CodigoOculto.Dao.PartidaDAO;
import co.edu.poli.CodigoOculto.Dao.Partida_JugadorDAO;
import co.edu.poli.CodigoOculto.Modelo.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
	@FXML
	private Text txtId12;

	private Jugador jugador1;

	private PartidaDAO partidaDAO;
	private Partida_JugadorDAO partidaJugadorDAO;

	private Temporizador temporizador;
	private Timeline timeline;

	private boolean juegoFinalizado = false;

	@FXML
	private ImageView imgJugador1;

	@FXML
	private ImageView imgJugador2;

	/**
	 * Click casilla
	 */
	@FXML
	private void seleccionarCasilla(MouseEvent event) {

		if (partidaVs == null || juegoFinalizado)
			return;

		Button b = (Button) event.getSource();

		Integer fila = GridPane.getRowIndex(b);
		Integer col = GridPane.getColumnIndex(b);

		Partida p = partidaVs.getPartidaActual();

		if (fila != null && fila == p.getFilaActual()) {
			p.moverCursorManual(col == null ? 0 : col);
		}
	}

	public void setConexion(Connection conexion) {
		this.partidaDAO = new PartidaDAO(conexion);
		this.partidaJugadorDAO = new Partida_JugadorDAO(conexion);
	}

	private void actualizarTurnoUI() {

		boolean turnoJ1 = partidaVs.getTurnoActual() == partidaVs.getJugador1();

		if (turnoJ1) {
			imgJugador1.setOpacity(1.0);
			imgJugador2.setOpacity(0.3);
		} else {
			imgJugador1.setOpacity(0.3);
			imgJugador2.setOpacity(1.0);
		}
	}

	/**
	 * Inicializa temporizador y teclado
	 */
	@FXML
	public void initialize() {
		temporizador = new Temporizador();
		Platform.runLater(this::configurarTeclado);
	}

	/**
	 * Asigna jugadores e inicia partida
	 */
	public void setJugadores(Jugador j1, Jugador j2) {

		this.jugador1 = j1;

		txtId.setText("J1: " + j1.getNombre());
		txtId1.setText("J2: " + j2.getNombre());

	}

	/**
	 * Configuración teclado
	 */
	private void configurarTeclado() {

		Scene scene = gridCasillas.getScene();
		if (scene == null)
			return;

		scene.setOnKeyPressed(e -> {

			if (partidaVs == null || juegoFinalizado)
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

		if (partidaVs == null || juegoFinalizado)
			return;

		if (!esTurnoActivo())
			return;

		Button btn = (Button) event.getSource();
		String t = btn.getText();

		Partida p = partidaVs.getPartidaActual();

		if (t.equalsIgnoreCase("Enter")) {

			if (!p.esFilaCompleta()) {
				mostrarAlertaTemporal("Completa toda la fila antes de continuar");
				return;
			}

			/**
			 * Valida que no existan ceros en la fila
			 */
			for (int i = 0; i < 5; i++) {

				if (p.getValorCelda(p.getFilaActual(), i).equals("0")) {

					mostrarAlertaTemporal("No puedes ingresar 0");
					return;
				}
			}

			manejarEnter();
			return;
		}

		int f = p.getFilaActual();
		int c = p.getColumnaActual();

		if (p.realizarIntento(t)) {
			Button b = getNodeActivo(f, c);
			if (b != null)
				b.setText(t);
		}
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

	/**
	 * Manejo de turno
	 */
	private void manejarEnter() {

		if (juegoFinalizado)
			return;

		Partida p = partidaVs.getPartidaActual();

		if (!p.esFilaCompleta())
			return;

		String estado = partidaVs.jugarTurno();

		if (estado.equals("FIN")) {

			juegoFinalizado = true;
			guardarHistorial();

			guardarHistorial();

			Partida p1 = partidaVs.getPartidaJ1();
			Partida p2 = partidaVs.getPartidaJ2();

			Jugador g = partidaVs.getGanador();

			String mensaje;

			if (g != null) {

				mensaje = "GANADOR DEL JUEGO: " + g.getNombre() + "\n\n" + "CODIGO J1: "
						+ convertirCodigo(p1.getCombinacion()) + "\n" + "CODIGO J2: "
						+ convertirCodigo(p2.getCombinacion());

			} else {

				mensaje = "EMPATE (AMBOS PERDIERON)\n\n" + "CODIGO J1: " + convertirCodigo(p1.getCombinacion()) + "\n"
						+ "CODIGO J2: " + convertirCodigo(p2.getCombinacion());
			}

			mostrarFinal(mensaje, false);

			detenerTemporizador();
			return;
		}

		if (estado.equals("EMPATE")) {

			juegoFinalizado = true;
			guardarHistorial();

			Partida p1 = partidaVs.getPartidaJ1();
			Partida p2 = partidaVs.getPartidaJ2();

			String mensaje = "EMPATE\n\n" + "CODIGO J1: " + convertirCodigo(p1.getCombinacion()) + "\n" + "CODIGO J2: "
					+ convertirCodigo(p2.getCombinacion());

			mostrarFinal(mensaje, false); // 👈 IMPORTANTE

			detenerTemporizador();
			return;
		}

		if (estado.equals("RONDA_COMPLETA")) {
			pintarRondaCompleta();
			partidaVs.siguienteRonda();
		}

		actualizarTurno();
		iniciarTemporizador();
	}

	/**
	 * Tiempo agotado
	 */
	private void iniciarTemporizador() {

		if (timeline != null)
			timeline.stop();

		temporizador.reiniciar();
		actualizarTiempo();

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

			temporizador.decrementar();
			actualizarTiempo();

			if (temporizador.tiempoAgotado() && partidaVs != null) {

				timeline.stop();

				Partida pAntes = partidaVs.getPartidaActual();
				String estado = partidaVs.tiempoAgotado();

				pintarFilaAnulada(pAntes.getFilaEvaluada(), pAntes);

				if (estado.equals("FIN")) {

					juegoFinalizado = true;
					guardarHistorial();

					Partida p1 = partidaVs.getPartidaJ1();
					Partida p2 = partidaVs.getPartidaJ2();

					Jugador g = partidaVs.getGanador();

					String mensaje;

					if (g != null) {

						mensaje = "GANADOR: " + g.getNombre() + "\n\n" + "CODIGO J1: "
								+ convertirCodigo(p1.getCombinacion()) + "\n" + "CODIGO J2: "
								+ convertirCodigo(p2.getCombinacion());

						mostrarFinal(mensaje, false);
					}

					else {

						mensaje = "FIN DEL JUEGO (SIN GANADOR)\n\n" + "CODIGO J1: "
								+ convertirCodigo(p1.getCombinacion()) + "\n" + "CODIGO J2: "
								+ convertirCodigo(p2.getCombinacion());

						mostrarFinal(mensaje, true);
					}

					detenerTemporizador();
					return;
				}

				if (estado.equals("RONDA_COMPLETA")) {
					pintarRondaCompleta();
					partidaVs.siguienteRonda();
				}

				actualizarTurno();
				iniciarTemporizador();
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public void setPartida(PartidaLocal_Vs partida) {
		this.partidaVs = partida;

		actualizarTurno();
		iniciarTemporizador();
	}

	/**
	 * Pintar ronda completa
	 */
	private void pintarRondaCompleta() {

		Partida p1 = partidaVs.getPartidaJ1();
		Partida p2 = partidaVs.getPartidaJ2();

		if (p1.getUltimoResultado() != null)
			pintarResultado(gridCasillas, p1.getFilaEvaluada(), p1.getUltimoResultado());

		if (p2.getUltimoResultado() != null)
			pintarResultado(gridCasillas1, p2.getFilaEvaluada(), p2.getUltimoResultado());
	}

	/**
	 * Convierte el código a texto
	 */
	private String convertirCodigo(int[] c) {

		if (c == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for (int n : c) {
			sb.append(n).append(" ");
		}

		return sb.toString().trim();
	}

	/**
	 * Popup final
	 */
	private void mostrarFinal(String mensaje, boolean empateSinAcierto) {

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		StringBuilder contenido = new StringBuilder(mensaje);

		Partida p1 = partidaVs.getPartidaJ1();
		Partida p2 = partidaVs.getPartidaJ2();

		if (empateSinAcierto && p1 != null && p2 != null) {

			contenido.append("\n\nCODIGO J1: ").append(convertirCodigo(p1.getCombinacion()));

			contenido.append("\nCODIGO J2: ").append(convertirCodigo(p2.getCombinacion()));
		}

		Label texto = new Label(contenido.toString());
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
		texto.setWrapText(true);

		StackPane root = new StackPane(texto);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 30;");

		Scene scene = new Scene(root, 500, 300);
		popup.setScene(scene);
		popup.show();

		PauseTransition pausa = new PauseTransition(Duration.seconds(5));

		pausa.setOnFinished(e -> {

			popup.close();
			volverMenu();

		});

		pausa.play();
	}

	private void volverMenu() {
		try {

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

			Parent root = loader.load();

			MenuJuegoControlador controller = loader.getController();
			controller.setJugador(jugador1);

			Stage stage = (Stage) gridCasillas.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void irMenuJugador(ActionEvent event) {

		Stage popup = new Stage();
		popup.initStyle(StageStyle.UNDECORATED);

		Label texto = new Label("¿Deseas volver al menú?");
		texto.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

		Button btnSalir = new Button("Salir");
		Button btnCancelar = new Button("Cancelar");

		btnSalir.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
		btnCancelar.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");

		StackPane root = new StackPane(texto);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 30;");

		root.getChildren().addAll(btnSalir, btnCancelar);

		StackPane.setAlignment(btnSalir, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(btnCancelar, Pos.BOTTOM_RIGHT);

		Scene scene = new Scene(root, 400, 200);
		popup.setScene(scene);
		popup.show();

		btnSalir.setOnAction(e -> {
			try {
				if (timeline != null)
					timeline.stop();
				if (partidaVs != null)
					juegoFinalizado = true;

				popup.close();

				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

				Parent rootMenu = loader.load();

				MenuJuegoControlador controller = loader.getController();
				controller.setJugador(jugador1);

				Stage stage = (Stage) gridCasillas.getScene().getWindow();
				stage.setScene(new Scene(rootMenu));
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnCancelar.setOnAction(e -> popup.close());
	}

	private void actualizarTiempo() {
		txtId12.setText(temporizador.getTiempoFormateado());
	}

	private void detenerTemporizador() {
		if (timeline != null)
			timeline.stop();
	}

	private boolean esTurnoActivo() {
		return partidaVs.getTurnoActual() == partidaVs.getJugador1() ? !gridCasillas.isDisabled()
				: !gridCasillas1.isDisabled();
	}

	private void actualizarTurno() {
		boolean j1 = partidaVs.getTurnoActual() == partidaVs.getJugador1();
		gridCasillas.setDisable(!j1);
		gridCasillas1.setDisable(j1);

		actualizarTurnoUI();
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

	private void pintarFilaAnulada(int fila, Partida partida) {

		GridPane grid = (partida == partidaVs.getPartidaJ1()) ? gridCasillas : gridCasillas1;

		for (int i = 0; i < 5; i++) {

			Button casilla = getNode(grid, fila, i);
			if (casilla == null)
				continue;

			casilla.setText("0");
			casilla.setStyle("-fx-background-color: gray;");
		}
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

	private void guardarHistorial() {

		int idPartida = partidaDAO.crearPartida();

		if (idPartida == -1) {
			System.out.println("Error al crear partida");
			return;
		}

		Jugador j1 = partidaVs.getJugador1();
		Jugador j2 = partidaVs.getJugador2();
		Jugador ganador = partidaVs.getGanador();

		if (ganador != null) {

			if (ganador.getId() == j1.getId()) {
				partidaJugadorDAO.guardar(idPartida, j1.getId(), "VICTORIA");
				partidaJugadorDAO.guardar(idPartida, j2.getId(), "DERROTA");
			} else {
				partidaJugadorDAO.guardar(idPartida, j2.getId(), "VICTORIA");
				partidaJugadorDAO.guardar(idPartida, j1.getId(), "DERROTA");
			}

		}

		else {

			partidaJugadorDAO.guardar(idPartida, j1.getId(), "DERROTA");
			partidaJugadorDAO.guardar(idPartida, j2.getId(), "DERROTA");
		}
	}

}
