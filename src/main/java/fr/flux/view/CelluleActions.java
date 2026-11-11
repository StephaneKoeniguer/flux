package fr.flux.view;

import atlantafx.base.theme.Styles;
import fr.flux.model.Torrent;
import fr.flux.moteur.Statut;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

/** La cellule de la colonne Actions : un bouton pause/lecture et une corbeille. */
class CelluleActions extends TableCell<Torrent, Statut> {

    private final FontIcon iconePause = new FontIcon(Feather.PAUSE);
    private final FontIcon iconeLecture = new FontIcon(Feather.PLAY);
    private final FontIcon iconeTermine = new FontIcon(Feather.CHECK_CIRCLE);
    private final Tooltip bullePause = new Tooltip("Mettre en pause");
    private final Tooltip bulleLecture = new Tooltip("Reprendre");
    private final Button boutonBascule = new Button();
    private final Button boutonSupprimer = new Button(null, new FontIcon(Feather.TRASH_2));
    private final HBox boutons = new HBox(4, boutonBascule, boutonSupprimer);

    CelluleActions(Consumer<Torrent> surBascule, Consumer<Torrent> surSuppression) {
        boutonBascule.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        boutonSupprimer.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.DANGER);
        boutonSupprimer.setTooltip(new Tooltip("Supprimer"));
        boutons.setAlignment(Pos.CENTER);

        boutonBascule.setOnAction(event -> {
            Torrent torrent = getTableRow().getItem();
            if (torrent != null) {
                surBascule.accept(torrent);
            }
        });
        boutonSupprimer.setOnAction(event -> {
            Torrent torrent = getTableRow().getItem();
            if (torrent != null) {
                surSuppression.accept(torrent);
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

}
