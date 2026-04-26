package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import co.edu.poli.CodigoOculto.Modelo.Partida;
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

    // Recibe jugador desde otra pantalla
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        actualizarTexto();
    }

    // Inicializa la vista
    @FXML
    public void initialize() {
        actualizarTexto();
    }

    // Ir a Como Jugar
    @FXML
    public void irAComoJugar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/ComoJugar.fxml")
            );

            Parent root = loader.load();

            ComoJugarControlador controller = loader.getController();
            controller.setJugador(jugador);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ir a juego individual
    @FXML
    public void irAPantallaJuego(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/PantallaJuego.fxml")
            );

            Parent root = loader.load();

            PantallaJuegoControlador controller = loader.getController();
            controller.setJugador(jugador);
            controller.setPartida(new Partida());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ir a modo 1vs1 local
    @FXML
    public void irA1vs1Local(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/_1vs1_Local.fxml")
            );

            Parent root = loader.load();

            _1vs1_LocalControlador controller = loader.getController();

            // solo jugador 1 por ahora
            controller.setJugador(jugador);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cerrar sesión
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

    // Actualiza textos en pantalla
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

    public Jugador getJugador() {
        return jugador;
    }
}