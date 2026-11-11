package fr.flux.view;

import fr.flux.model.ListeTorrents;
import fr.flux.model.Torrent;
import fr.flux.moteur.MoteurTorrent;
import fr.flux.moteur.Statut;
import fr.flux.util.Format;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.input.KeyCode;

import java.util.List;

public class TableTorrents extends TableView<Torrent> {

    private final MoteurTorrent moteur;

    public TableTorrents(ListeTorrents liste, MoteurTorrent moteur) {
        this.moteur = moteur;

        // La table trie une copie triée de la liste, sans jamais modifier l'originale
        SortedList<Torrent> listeTriee = new SortedList<>(liste.getTorrents());
        listeTriee.comparatorProperty().bind(comparatorProperty());
        setItems(listeTriee);

        getColumns().setAll(creerColonnes());
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        setPlaceholder(new Label("Aucun torrent. Colle un lien magnet ci-dessus pour commencer."));
        setContextMenu(creerMenu());

        // Touche Supprimer (sur Mac, la touche "delete" s'appelle BACK_SPACE)
        setOnKeyPressed(event -> {
            Torrent selection = getSelectionModel().getSelectedItem();
            if (selection != null && (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)) {
                supprimer(selection);
            }
        });
    }

    private List<TableColumn<Torrent, ?>> creerColonnes() {
        TableColumn<Torrent, String> nom = new TableColumn<>("Nom");
        nom.setCellValueFactory(cellule -> cellule.getValue().nomProperty());
        nom.setPrefWidth(300);

        TableColumn<Torrent, Number> taille = new TableColumn<>("Taille");
        taille.setCellValueFactory(cellule -> cellule.getValue().tailleProperty());
        taille.setCellFactory(colonne -> new CelluleFormatee(Format::octets));
        taille.getStyleClass().add("nombre");

        TableColumn<Torrent, Double> progression = new TableColumn<>("Progression");
        progression.setCellValueFactory(cellule -> cellule.getValue().progressionProperty().asObject());
        progression.setCellFactory(ProgressBarTableCell.forTableColumn());
        progression.setPrefWidth(160);

        TableColumn<Torrent, Statut> statut = new TableColumn<>("Statut");
        statut.setCellValueFactory(cellule -> cellule.getValue().statutProperty());

        TableColumn<Torrent, Number> vitesse = new TableColumn<>("Vitesse");
        vitesse.setCellValueFactory(cellule -> cellule.getValue().vitesseProperty());
        vitesse.setCellFactory(colonne -> new CelluleFormatee(v -> v == 0 ? "" : Format.octets(v) + "/s"));
        vitesse.getStyleClass().add("nombre");

        TableColumn<Torrent, Number> pairs = new TableColumn<>("Pairs");
        pairs.setCellValueFactory(cellule -> cellule.getValue().pairsProperty());
        pairs.getStyleClass().add("nombre");

        TableColumn<Torrent, Statut> actions = new TableColumn<>("Actions");
        actions.setCellValueFactory(cellule -> cellule.getValue().statutProperty());
        actions.setCellFactory(colonne -> new CelluleActions(this::basculer, this::supprimer));
        actions.setSortable(false);
        actions.setReorderable(false);
        actions.setMinWidth(90);
        actions.setMaxWidth(90);

        return List.of(nom, taille, progression, statut, vitesse, pairs, actions);
    }

    private ContextMenu creerMenu() {
        MenuItem pause = new MenuItem("Mettre en pause");
        MenuItem reprise = new MenuItem("Reprendre");
        MenuItem suppression = new MenuItem("Supprimer…");

        pause.setOnAction(e -> moteur.mettreEnPause(getSelectionModel().getSelectedItem().getId()));
        reprise.setOnAction(e -> moteur.reprendre(getSelectionModel().getSelectedItem().getId()));
        suppression.setOnAction(e -> supprimer(getSelectionModel().getSelectedItem()));

        ContextMenu menu = new ContextMenu(pause, reprise, new SeparatorMenuItem(), suppression);
        menu.setOnShowing(e -> {
            Torrent selection = getSelectionModel().getSelectedItem();
            boolean aucune = selection == null;
            boolean enPause = !aucune && selection.getStatut() == Statut.PAUSE;
            pause.setDisable(aucune || enPause);
            reprise.setDisable(aucune || !enPause);
            suppression.setDisable(aucune);
        });
        return menu;
    }

    private void basculer(Torrent torrent) {
        if (torrent.getStatut() == Statut.PAUSE) {
            moteur.reprendre(torrent.getId());
        } else {
            moteur.mettreEnPause(torrent.getId());
        }
    }

    private void supprimer(Torrent torrent) {
        DialogueSuppression.demander(getScene().getWindow(), torrent.getNom())
                .ifPresent(avecFichiers -> moteur.supprimer(torrent.getId(), avecFichiers));
    }
}
