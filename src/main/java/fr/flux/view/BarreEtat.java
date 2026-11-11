package fr.flux.view;

import fr.flux.model.ListeTorrents;
import fr.flux.util.Format;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class BarreEtat extends HBox {

    private final Label message = new Label("Prêt");

    public BarreEtat(ListeTorrents liste) {
        super(8);   // appelle le constructeur de HBox : 8 px entre les enfants

        Label descendant = new Label();
        descendant.textProperty().bind(Bindings.createStringBinding(
                () -> "↓ " + Format.octets(liste.getDebitDescendant()) + "/s",
                liste.debitDescendantProperty()));

        Label montant = new Label();
        montant.textProperty().bind(Bindings.createStringBinding(
                () -> "↑ " + Format.octets(liste.getDebitMontant()) + "/s",
                liste.debitMontantProperty()));

        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);

        getChildren().addAll(message, espace, descendant, montant);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("barre-etat");
    }

    public void afficher(String texte) {
        message.setText(texte);
    }
}
