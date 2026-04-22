package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuJuegoControlador {

    private Jugador jugador;

    @FXML
    private Text textoNombre;

    @FXML
    private Text textoId;

    // =========================
    // RECIBIR JUGADOR
    // =========================
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        actualizarTexto();
    }

    // =========================
    // INIT
    // =========================
    @FXML
    public void initialize() {
        actualizarTexto();
    }
    // =========================
    // Como jugar
    // =========================
    
    @FXML
    public void irAComoJugar(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/ComoJugar.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // MOSTRAR DATOS
    // =========================
    private void actualizarTexto() {
        if (jugador != null) {

            if (textoNombre != null) {
                textoNombre.setText("Name: " + jugador.getNombre());
            }

            if (textoId != null) {
                textoId.setText("ID: " + jugador.getId());
            }
        }
    }

    // =========================
    // BOTÓN: IR A PANTALLA JUEGO
    // =========================
    @FXML
    public void irAPantallaJuego(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/PantallaJuego.fxml")
            );

            Parent root = loader.load();

            PantallaJuegoControlador controller = loader.getController();
            controller.setJugador(jugador);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // BOTÓN: CERRAR SESIÓN
    // =========================
    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuPrincipal.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // GETTER
    // =========================
    public Jugador getJugador() {
        return jugador;
    }
}