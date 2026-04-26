package co.edu.poli.CodigoOculto.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import co.edu.poli.CodigoOculto.Modelo.PartidaLocal_Vs;
import co.edu.poli.CodigoOculto.Modelo.Jugador;

public class PantallaJuego1vs1Controlador {

	@FXML private GridPane gridCasillas;
	@FXML private GridPane gridCasillas1;

	@FXML private Text txtId;
	@FXML private Text txtId1;

	private PartidaLocal_Vs partidaVS;
	private Button casillaSeleccionada;

	// recibir jugadores
	public void setJugadores(Jugador j1, Jugador j2) {

		partidaVS = new PartidaLocal_Vs();
		partidaVS.iniciar(j1, j2);

		if (txtId != null && j1 != null) {
			txtId.setText(j1.getNombre());
		}

		if (txtId1 != null && j2 != null) {
			txtId1.setText(j2.getNombre());
		}
	}

	// seleccionar casilla
	@FXML
	public void seleccionarCasilla(javafx.scene.input.MouseEvent event) {
		casillaSeleccionada = (Button) event.getSource();
	}

	// presionar botones
	@FXML
	public void presionarBoton(ActionEvent event) {

		if (partidaVS == null) return;

		Button btn = (Button) event.getSource();
		String valor = btn.getText();

		if (!valor.equals("Enter")) {

			partidaVS.getPartidaActual().realizarIntento(valor);

			if (casillaSeleccionada != null) {
				casillaSeleccionada.setText(valor);
			}

		} else {

			String estado = partidaVS.jugarTurno();

			if (estado.equals("FIN")) {
				System.out.println("Ganador: " + partidaVS.getGanador().getNombre());
			}

			if (estado.equals("RONDA_COMPLETA")) {
				partidaVS.siguienteRonda();
			}
		}
	}

	// volver al menu
	@FXML
	public void irMenuJugador(ActionEvent event) {

		try {

			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml")
			);

			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}