package fr.flux.view;

import atlantafx.base.theme.Styles;
import fr.flux.moteur.MoteurTorrent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

public class BarreAjout extends HBox {

    private final TextField champLien = new TextField();
    private final MoteurTorrent moteur;
    private final Consumer<String> messager;

    /** @param messager où afficher les messages pour l'utilisateur (la barre d'état, par exemple) */
    public BarreAjout(MoteurTorrent moteur, Consumer<String> messager) {
        super(8);
        this.moteur = moteur;
        this.messager = messager;

        champLien.setPromptText("Colle un lien magnet");
        champLien.textProperty().addListener((obs, ancien, nouveau) ->
                champLien.pseudoClassStateChanged(Styles.STATE_DANGER, false));
        HBox.setHgrow(champLien, Priority.ALWAYS);

        Button boutonAjouter = new Button("Ajouter");
        boutonAjouter.setDefaultButton(true);
        boutonAjouter.setOnAction(event -> ajouterLien());

        Button boutonOuvrir = new Button("Ouvrir un .torrent");

        getChildren().addAll(champLien, boutonAjouter, boutonOuvrir);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(4));
        getStyleClass().add("barre-ajout");
    }

    private void ajouterLien() {
        String lien = champLien.getText().strip();
        champLien.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        if (!lien.startsWith("magnet:?")) {
            messager.accept("Ce n'est pas un lien magnet valide");
            champLien.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return;
        }
        try {
            moteur.ajouterMagnet(lien);
            messager.accept("Torrent ajouté");
            champLien.clear();
        } catch (IllegalArgumentException e) {
            messager.accept("Lien magnet invalide : " + e.getMessage());
            champLien.pseudoClassStateChanged(Styles.STATE_DANGER, true);
        }
    }

}
