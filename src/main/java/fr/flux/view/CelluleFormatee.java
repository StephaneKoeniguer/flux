package fr.flux.view;

import fr.flux.model.Torrent;
import javafx.scene.control.TableCell;

import java.util.function.LongFunction;

/** Une cellule qui affiche un nombre après l'avoir passé dans une fonction de formatage. */
class CelluleFormatee extends TableCell<Torrent, Number> {

    private final LongFunction<String> formater;

    CelluleFormatee(LongFunction<String> formater) {
        this.formater = formater;
    }

    @Override
    protected void updateItem(Number valeur, boolean vide) {
        super.updateItem(valeur, vide);
        setText(vide || valeur == null ? null : formater.apply(valeur.longValue()));
    }

}
