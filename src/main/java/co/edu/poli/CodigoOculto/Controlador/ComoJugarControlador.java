package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ComoJugarControlador {

	private Jugador jugador;

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	@FXML
	private void irMenuJugador(ActionEvent event) {

		try {

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml"));

			Parent root = loader.load();

			MenuJuegoControlador controller = loader.getController();
			controller.setJugador(jugador); //

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}