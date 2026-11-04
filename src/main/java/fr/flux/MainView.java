package fr.flux;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

public class MainView extends BorderPane {


    private final TextField champLien = new TextField();
    private final Label etat = new Label("Prêt");
    private final Label debitDescendant = new Label("↓ 0 o/s");
    private final Label debitMontant = new Label("↑ 0 o/s");

    public MainView() {
        setTop(creerBareAjout());
        setCenter(creerListe());
        setBottom(creerBarreEtat());
    }

    private HBox creerBareAjout() {
        champLien.setPromptText("Colle un lien magnet");
        champLien.textProperty().addListener((obs, ancien, nouveau) ->
                champLien.pseudoClassStateChanged(Styles.STATE_DANGER, false));
        HBox.setHgrow(champLien, Priority.ALWAYS);

        Button boutonAjouter = new Button("Ajouter");
        boutonAjouter.setDefaultButton(true);
        boutonAjouter.setOnAction(event -> ajouterLien());

        Button BoutonOuvrir = new Button("Ouvrir un Torrent");

        HBox barre = new HBox(8, champLien,boutonAjouter, BoutonOuvrir);
        barre.setAlignment(Pos.CENTER_LEFT);
        barre.setPadding(new Insets(4));
        barre.getStyleClass().add("barre-ajout");
        return barre;
    }

    private TableView<Object> creerListe() {
        TableView<Object> table = new TableView<>();
        TableColumn<Object, String> colonneNom = new TableColumn<>("Nom");
        TableColumn<Object, String> colonneTaille = new TableColumn<>("Taille");
        TableColumn<Object, String> colonneProgression = new TableColumn<>("Progression");
        TableColumn<Object, String> colonneStatut = new TableColumn<>("Statut");
        TableColumn<Object, String> colonneVitesse = new TableColumn<>("Vitesse");

        colonneProgression.setPrefWidth(160);
        colonneNom.setPrefWidth(360);

        table.getColumns().setAll(List.of(colonneNom, colonneTaille, colonneProgression, colonneStatut, colonneVitesse));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("Aucun torrent. Colle un lien magnet ci-dessus pour commencer."));

        return table;
    }

    private HBox creerBarreEtat() {
        Region espace = new Region();
        HBox.setHgrow(espace,Priority.ALWAYS);

        HBox barre = new HBox(8, etat, espace, debitDescendant, debitMontant);
        barre.setAlignment(Pos.CENTER_LEFT);
        barre.getStyleClass().add("barre-etat");

        return barre;
    }

    private void ajouterLien() {
        String lien = champLien.getText().strip();
        champLien.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        if (!lien.startsWith("magnet:?")) {
            etat.setText("Ce n'est pas un lien magnet valide");
            champLien.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return;
        }
        etat.setText("Lien reçu !");
        champLien.clear();
    }

}
