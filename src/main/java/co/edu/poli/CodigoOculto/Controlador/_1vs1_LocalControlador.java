package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class _1vs1_LocalControlador {

    private Jugador jugador1;
    private Jugador jugador2;

    @FXML
    private TextField txtId;

    @FXML
    private Text textoNombre; // Debe coincidir con fx:id del FXML

    public void setJugador(Jugador jugador) {
        this.jugador1 = jugador;
        actualizarTexto(); // Se llama también aquí por el orden de carga de JavaFX
    }

    @FXML
    public void initialize() {
        actualizarTexto();
    }

    private void actualizarTexto() {
        if (textoNombre != null && jugador1 != null) {
            textoNombre.setText("Name J1: " + jugador1.getNombre());
        }
    }

    @FXML
    public void iniciarPartida(ActionEvent event) {
        try {

            String idTexto = txtId.getText();
            if (idTexto == null || idTexto.isBlank() || jugador1 == null) return;

            int idInt;
            try {
                idInt = Integer.parseInt(idTexto);
            } catch (NumberFormatException e) {
                return;
            }

            jugador2 = new Jugador();
            jugador2.setId(idInt);
            jugador2.setNombre("Jugador 2");
            jugador2.setPuntaje(0);
            jugador2.setEsInvitado(false);

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/PantallaJuego1vs1.fxml")
            );

            Parent root = loader.load();

            PantallaJuego1vs1Controlador controller = loader.getController();
            controller.setJugadores(jugador1, jugador2);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void volverMenu(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml")
            );

            Parent root = loader.load();

            MenuJuegoControlador controller = loader.getController();
            controller.setJugador(jugador1); // Para no perder datos

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}