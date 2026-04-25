package co.edu.poli.CodigoOculto.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuPrincipal.fxml"));
			System.out.println(getClass().getResource("/co/edu/poli/CodigoOculto/Vista/MenuPrincipal.fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);

			stage.setTitle("Secret Code - Código Oculto");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			System.out.println("❌ Error al cargar el FXML:");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}