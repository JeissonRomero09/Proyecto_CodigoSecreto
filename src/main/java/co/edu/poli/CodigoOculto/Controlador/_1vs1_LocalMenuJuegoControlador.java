package co.edu.poli.CodigoOculto.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import co.edu.poli.CodigoOculto.Modelo.Jugador;

public class _1vs1_LocalMenuJuegoControlador {

    @FXML
    private Text textoNombre;

    @FXML
    private Text textoNombre1;

    private Jugador jugador1;
    private Jugador jugador2;

    // Recibe jugadores
    public void setJugadores(Jugador j1, Jugador j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;

        if (textoNombre != null && j1 != null) {
            textoNombre.setText("Name J1: " + j1.getNombre());
        }

        if (textoNombre1 != null && j2 != null) {
            textoNombre1.setText("Name J2: " + j2.getNombre());
        } else if (textoNombre1 != null) {
            textoNombre1.setText("Name J2: Sin jugador");
        }
    }

    // Ir a pantalla de juego
    @FXML
    public void irAPantallaJuego(ActionEvent event) {
        try {
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

    // Ir a como jugar
    @FXML
    public void irAComoJugar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/ComoJugar.fxml")
            );

            Parent root = loader.load();

            ComoJugarControlador controller = loader.getController();
            controller.setJugador(jugador1);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cerrar sesión jugador 2
    @FXML
    public void cerrarSesion(ActionEvent event) {
        jugador2 = null;

        if (textoNombre1 != null) {
            textoNombre1.setText("Name J2:");
        }
    }
}