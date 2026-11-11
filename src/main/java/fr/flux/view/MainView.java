package fr.flux.view;

import atlantafx.base.theme.Styles;
import fr.flux.model.Torrent;
import fr.flux.moteur.EtatTorrent;
import fr.flux.moteur.MoteurTorrent;
import fr.flux.moteur.Statut;
import fr.flux.util.Format;
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
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;
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
    private final MoteurTorrent moteur;
    private final Map<String, Torrent> torrentsParId =new HashMap<>();

    public MainView(MoteurTorrent moteur) {
        this.moteur = moteur;
        setTop(creerBareAjout());
        setCenter(creerListe());
        setBottom(creerBarreEtat());
        moteur.surMiseAJour(etats -> Platform.runLater(() -> appliquer(etats)));
        moteur.surSuppression(id -> Platform.runLater(() -> retirer(id)));
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
        ContextMenu menu = creerMenu(table);
        table.setContextMenu(menu);

        TableColumn<Torrent, String> colonneNom = new TableColumn<>("Nom");
        colonneNom.setCellValueFactory(cellule -> cellule.getValue().nomProperty());
        colonneNom.setPrefWidth(300);

        TableColumn<Torrent, Number> colonneTaille = new TableColumn<>("Taille");
        colonneTaille.setCellValueFactory(cellule -> cellule.getValue().tailleProperty());
        colonneTaille.setCellFactory(colonne -> celluleFormatee(Format::octets));
        colonneTaille.getStyleClass().add("nombre");

        TableColumn<Torrent, Double> colonneProgression = new TableColumn<>("Progression");
        colonneProgression.setCellValueFactory(cellule -> cellule.getValue().progressionProperty().asObject());
        colonneProgression.setCellFactory(ProgressBarTableCell.forTableColumn());
        colonneProgression.setPrefWidth(160);

        TableColumn<Torrent, Statut> colonneStatut = new TableColumn<>("Statut");
        colonneStatut.setCellValueFactory(cellule -> cellule.getValue().statutProperty());

        TableColumn<Torrent, Number> colonneVitesse = new TableColumn<>("Vitesse");
        colonneVitesse.setCellValueFactory(cellule -> cellule.getValue().vitesseProperty());
        colonneVitesse.setCellFactory(colonne -> celluleFormatee(v -> v == 0 ? "" : Format.octets(v) + "/s"));
        colonneVitesse.getStyleClass().add("nombre");

        TableColumn<Torrent, Number> colonnePairs = new TableColumn<>("Pairs");
        colonnePairs.setCellValueFactory(cellule -> cellule.getValue().pairsProperty());
        colonnePairs.getStyleClass().add("nombre");

        TableColumn<Torrent, Statut> colonneActions = new TableColumn<>("Actions");
        colonneActions.setCellValueFactory(cellule -> cellule.getValue().statutProperty());
        colonneActions.setSortable(false);
        colonneActions.setReorderable(false);
        colonneActions.setMinWidth(90);
        colonneActions.setMaxWidth(90);
        colonneActions.setCellFactory(colonne -> new TableCell<>() {

            private final FontIcon iconePause = new FontIcon(Feather.PAUSE);
            private final FontIcon iconeLecture = new FontIcon(Feather.PLAY);
            private final Tooltip bullePause = new Tooltip("Mettre en pause");
            private final Tooltip bulleLecture = new Tooltip("Reprendre");
            private final FontIcon iconeTermine = new FontIcon(Feather.CHECK_CIRCLE);
            private final Tooltip bulleTermine = new Tooltip("Terminé");

            private final Button boutonBascule = new Button();
            private final Button boutonSupprimer = new Button(null, new FontIcon(Feather.TRASH_2));
            private final HBox boutons = new HBox(4, boutonBascule, boutonSupprimer);

            {
                boutonBascule.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                boutonSupprimer.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.DANGER);
                boutonSupprimer.setTooltip(new Tooltip("Supprimer"));
                boutons.setAlignment(Pos.CENTER);

                boutonBascule.setOnAction(event -> {
                    Torrent torrent = getTableRow().getItem();
                    if (torrent == null) {
                        return;
                    }
                    if (torrent.getStatut() == Statut.PAUSE) {
                        moteur.reprendre(torrent.getId());
                    } else {
                        moteur.mettreEnPause(torrent.getId());
                    }
                });

                boutonSupprimer.setOnAction(event -> {
                    Torrent torrent = getTableRow().getItem();
                    if (torrent != null) {
                        confirmerSuppression(torrent);
                    }
                });
            }

            @Override
            protected void updateItem(Statut statut, boolean vide) {
                super.updateItem(statut, vide);
                if (vide || statut == null) {
                    setGraphic(null);
                    return;
                }
                boutonBascule.setDisable(false);
                if (statut == Statut.TERMINE) {
                    boutonBascule.setGraphic(iconeTermine);
                    boutonBascule.setTooltip(bulleTermine);
                    boutonBascule.setDisable(true);
                } else if (statut == Statut.PAUSE) {
                    boutonBascule.setGraphic(iconeLecture);
                    boutonBascule.setTooltip(bulleLecture);
                } else {
                    boutonBascule.setGraphic(iconePause);
                    boutonBascule.setTooltip(bullePause);
                }
                setGraphic(boutons);
            }
        });


        table.getColumns().setAll(List.of(colonneNom, colonneTaille, colonneProgression, colonneStatut, colonneVitesse, colonnePairs, colonneActions));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("Aucun torrent. Colle un lien magnet ci-dessus pour commencer."));

        // Touche Supprimer (sur Mac, la touche "delete" s'appelle BACK_SPACE)
        table.setOnKeyPressed(event -> {
            Torrent selection = table.getSelectionModel().getSelectedItem();
            if (selection != null && (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)) {
                confirmerSuppression(selection);
            }
        });


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

        try {
            moteur.ajouterMagnet(lien);
            etat.setText("Torrent ajouté");
            champLien.clear();
        } catch (IllegalArgumentException e) {
            etat.setText("Lien magnet invalide : " + e.getMessage());
            champLien.pseudoClassStateChanged(Styles.STATE_DANGER, true);
        }
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

    // Exécutée sur le thread JavaFX grâce à Platform.runLater.
    private void appliquer(List<EtatTorrent> etats) {
        for (EtatTorrent etat : etats) {
            Torrent torrent = torrentsParId.get(etat.id());
            if (torrent == null) {
                torrent = new Torrent(etat.id());
                torrentsParId.put(etat.id(), torrent);
                torrents.add(torrent);
            }
            torrent.mettreAJour(etat);
        }

        recalculerDebits();
    }


    private void retirer(String id) {
        Torrent torrent = torrentsParId.remove(id);
        if (torrent != null) {
            torrents.remove(torrent);
        }
        recalculerDebits();
    }

    private void recalculerDebits() {
        long totalDescendant = 0;
        long totalMontant = 0;
        for (Torrent torrent : torrents) {
            totalDescendant += torrent.getVitesse();
            totalMontant += torrent.getVitesseMontante();
        }
        debitDescendant.setText("↓ " + Format.octets(totalDescendant) + "/s");
        debitMontant.setText("↑ " + Format.octets(totalMontant) + "/s");
    }


    private ContextMenu creerMenu(TableView<Torrent> table) {
        MenuItem pause = new MenuItem("Mettre en pause");
        MenuItem reprise = new MenuItem("Reprendre");
        MenuItem supprimer = new MenuItem("Supprimer…");

        pause.setOnAction(e -> moteur.mettreEnPause(table.getSelectionModel().getSelectedItem().getId()));
        reprise.setOnAction(e -> moteur.reprendre(table.getSelectionModel().getSelectedItem().getId()));
        supprimer.setOnAction(e -> confirmerSuppression(table.getSelectionModel().getSelectedItem()));

        ContextMenu menu = new ContextMenu(pause, reprise, new SeparatorMenuItem(), supprimer);

        // Juste avant l'affichage, on active seulement les actions qui ont du sens
        menu.setOnShowing(e -> {
            Torrent selection = table.getSelectionModel().getSelectedItem();
            boolean aucune = selection == null;
            boolean enPause = !aucune && selection.getStatut() == Statut.PAUSE;
            pause.setDisable(aucune || enPause);
            reprise.setDisable(aucune || !enPause);
            supprimer.setDisable(aucune);
        });
        return menu;
    }

    private void confirmerSuppression(Torrent torrent) {
        CheckBox avecFichiers = new CheckBox("Supprimer aussi les fichiers téléchargés");
        ButtonType boutonSupprimer = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);

        Alert boite = new Alert(Alert.AlertType.CONFIRMATION);
        boite.initOwner(getScene().getWindow());     // la boîte s'affiche au-dessus de notre fenêtre
        boite.setTitle("Supprimer le torrent");
        boite.setHeaderText("Retirer « " + torrent.getNom() + " » de la liste ?");
        boite.getDialogPane().setContent(avecFichiers);
        boite.getButtonTypes().setAll(ButtonType.CANCEL, boutonSupprimer);
        boite.getDialogPane().lookupButton(boutonSupprimer).getStyleClass().add(Styles.DANGER);

        boite.showAndWait()
                .filter(bouton -> bouton == boutonSupprimer)
                .ifPresent(bouton -> moteur.supprimer(torrent.getId(), avecFichiers.isSelected()));
    }

}
