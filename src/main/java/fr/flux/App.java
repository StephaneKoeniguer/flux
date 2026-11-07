package fr.flux;

import atlantafx.base.theme.NordDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class App extends Application {

    private MoteurTorrent moteur;

    @Override
    public void init(){
        // Initialisation hors interface
        File dossier = Path.of(System.getProperty("user.home"), "Downloads", "Flux").toFile();
        moteur = new MoteurTorrent(dossier);
        moteur.demarrer();
    }


    @Override
    public void start(Stage stage) {
        // Permet de charger un thème (atlantafx)
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        Scene scene = new Scene(new MainView(moteur), 960, 600);
        // Liaison feuille de style externe
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Flux");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        moteur.arreter();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
