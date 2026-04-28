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
        cambiarEscena(
            event,
            "/co/edu/poli/CodigoOculto/Vista/ComoJugar.fxml",
            (ComoJugarControlador controller) -> controller.setJugador(jugador)
        );
    }

    //Jugar solo (Partida)
    @FXML
    public void irAPantallaJuego(ActionEvent event) {
        cambiarEscena(
            event,
            "/co/edu/poli/CodigoOculto/Vista/PantallaJuego.fxml",
            (PantallaJuegoControlador controller) -> {
                controller.setJugador(jugador);
                controller.setPartida(new Partida()); 
            }
        );
    }

    // 1vs1 local
    @FXML
    public void irA1vs1Local(ActionEvent event) {
        cambiarEscena(
            event,
            "/co/edu/poli/CodigoOculto/Vista/_1vs1_Local.fxml",
            (_1vs1_LocalControlador controller) -> controller.setJugador(jugador)
        );
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

   
    private <T> void cambiarEscena(ActionEvent event, String rutaFXML, ControllerSetter<T> setter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            T controller = loader.getController();

            if (setter != null) {
                setter.set(controller);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // interfaz funcional
    private interface ControllerSetter<T> {
        void set(T controller);
    }

    // Actualiza textos
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