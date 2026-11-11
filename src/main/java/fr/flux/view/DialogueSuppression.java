package fr.flux.view;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.stage.Window;

import java.util.Optional;

final class DialogueSuppression {

    private DialogueSuppression() { }

    /**
     * Demande confirmation à l'utilisateur.
     * @return vide si l'utilisateur annule ; sinon true s'il faut aussi supprimer les fichiers
     */
    static Optional<Boolean> demander(Window proprietaire, String nomTorrent) {
        CheckBox avecFichiers = new CheckBox("Supprimer aussi les fichiers téléchargés");
        ButtonType boutonSupprimer = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);

        Alert boite = new Alert(Alert.AlertType.CONFIRMATION);
        boite.initOwner(proprietaire);
        boite.setTitle("Supprimer le torrent");
        boite.setHeaderText("Retirer « " + nomTorrent + " » de la liste ?");
        boite.getDialogPane().setContent(avecFichiers);
        boite.getButtonTypes().setAll(ButtonType.CANCEL, boutonSupprimer);
        boite.getDialogPane().lookupButton(boutonSupprimer).getStyleClass().add(Styles.DANGER);

        return boite.showAndWait()
                .filter(bouton -> bouton == boutonSupprimer)
                .map(bouton -> avecFichiers.isSelected());
    }

}
