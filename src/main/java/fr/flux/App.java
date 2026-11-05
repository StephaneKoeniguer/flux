package fr.flux;

import atlantafx.base.theme.NordDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {

        // Permet de charger un thème (atlantafx)
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        Scene scene = new Scene(new MainView(), 960, 600);
        // Liaison feuille de style externe
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Flux");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
