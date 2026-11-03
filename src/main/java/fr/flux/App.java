package fr.flux;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {

        Label titre = new Label("Flux");
        titre.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label sousTitre = new Label("Aucun torrent pour l'instant");

        Button boutonAjouter =  new Button("Ajouter un torrent");
        boutonAjouter.setOnAction(event -> sousTitre.setText("Bientôt !"));

        VBox racine = new VBox(8, titre, sousTitre, boutonAjouter);   // empile ses enfants verticalement, 8 px d'écart
        racine.setAlignment(Pos.CENTER);
        racine.setPadding(new Insets(24));

        Scene scene = new Scene(racine, 900, 600);

        stage.setTitle("Flux");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
