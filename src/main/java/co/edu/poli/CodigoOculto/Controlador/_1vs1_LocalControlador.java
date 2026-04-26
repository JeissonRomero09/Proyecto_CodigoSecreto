package co.edu.poli.CodigoOculto.Controlador;

import co.edu.poli.CodigoOculto.Modelo.Jugador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class _1vs1_LocalControlador {

    private Jugador jugador1;
    private Jugador jugador2;

    @FXML
    private TextField txtId;

    // Recibe jugador 1 desde menú
    public void setJugador(Jugador jugador) {
        this.jugador1 = jugador;
    }

    // Iniciar partida 1vs1
    @FXML
    public void iniciarPartida(ActionEvent event) {
        try {

            // validar entrada
            String idTexto = txtId.getText();
            if (idTexto == null || idTexto.isBlank() || jugador1 == null) return;

            int idInt;

            // convertir id a int
            try {
                idInt = Integer.parseInt(idTexto);
            } catch (NumberFormatException e) {
                return; // id inválido
            }

            // crear jugador 2 usando constructor válido
            jugador2 = new Jugador();
            jugador2.setId(idInt);
            jugador2.setNombre("Jugador 2");
            jugador2.setPuntaje(0);
            jugador2.setEsInvitado(false);

            // cargar vista de juego
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

    // volver al menú
    @FXML
    public void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuJuego.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}