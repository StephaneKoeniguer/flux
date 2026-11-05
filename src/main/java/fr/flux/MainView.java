package fr.flux;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.function.LongFunction;

/**
 * Interface Utilisateur
 */
public class MainView extends BorderPane {

    private final TextField champLien = new TextField();
    private final Label etat = new Label("Prêt");
    private final Label debitDescendant = new Label("↓ 0 o/s");
    private final Label debitMontant = new Label("↑ 0 o/s");
    private final ObservableList<Torrent> torrents = FXCollections.observableArrayList();

    public MainView() {
        setTop(creerBareAjout());
        setCenter(creerListe());
        setBottom(creerBarreEtat());
    }

    /**
     * Barre d'ajout
     * @return
     */
    private HBox creerBareAjout() {
        champLien.setPromptText("Colle un lien magnet");
        //Listener sur le textField (lien)
        champLien.textProperty().addListener((obs, ancien, nouveau) ->
                champLien.pseudoClassStateChanged(Styles.STATE_DANGER, false));

        HBox.setHgrow(champLien, Priority.ALWAYS);

        Button boutonAjouter = new Button("Ajouter");
        boutonAjouter.setDefaultButton(true);
        boutonAjouter.setOnAction(event -> ajouterLien());

        Button BoutonOuvrir = new Button("Ouvrir un Torrent");

        HBox barre = new HBox(8, champLien, boutonAjouter, BoutonOuvrir);
        barre.setAlignment(Pos.CENTER_LEFT);
        barre.setPadding(new Insets(4));
        barre.getStyleClass().add("barre-ajout");
        return barre;
    }

    private TableView<Torrent> creerListe() {
        TableView<Torrent> table = new TableView<>(torrents);

        TableColumn<Torrent, String> colonneNom = new TableColumn<>("Nom");
        colonneNom.setCellValueFactory(cellule -> cellule.getValue().nomProperty());
        colonneNom.setPrefWidth(300);

        TableColumn<Torrent, Number> colonneTaille = new TableColumn<>("Taille");
        colonneTaille.setCellValueFactory(cellule -> cellule.getValue().tailleProperty());
        colonneTaille.setCellFactory(colonne -> celluleFormatee(Format::octets));

        TableColumn<Torrent, Double> colonneProgression = new TableColumn<>("Progression");
        colonneProgression.setCellValueFactory(cellule -> cellule.getValue().progressionProperty().asObject());
        colonneProgression.setCellFactory(ProgressBarTableCell.forTableColumn());
        colonneNom.setPrefWidth(160);

        TableColumn<Torrent, Torrent.Statut> colonneStatut = new TableColumn<>("Statut");
        colonneStatut.setCellValueFactory(cellule -> cellule.getValue().statutProperty());

        TableColumn<Torrent, Number> colonneVitesse = new TableColumn<>("Vitesse");
        colonneVitesse.setCellValueFactory(cellule -> cellule.getValue().vitesseProperty());
        colonneVitesse.setCellFactory(colonne -> celluleFormatee(v -> v == 0 ? "" : Format.octets(v) + "/s"));

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
        /*String lien = champLien.getText().strip();
        champLien.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        if (!lien.startsWith("magnet:?")) {
            etat.setText("Ce n'est pas un lien magnet valide");
            champLien.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return;
        }
        etat.setText("Lien reçu !");
        champLien.clear();*/
    }

    // Une cellule qui affiche un nombre après l'avoir passé dans une fonction de formatage.
    private static TableCell<Torrent, Number> celluleFormatee(LongFunction<String> formater) {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number valeur, boolean vide) {
                super.updateItem(valeur, vide);
                setText(vide || valeur == null ? null : formater.apply(valeur.longValue()));
            }
        };
    }

}
