package fr.flux.view;

import fr.flux.model.ListeTorrents;
import fr.flux.moteur.MoteurTorrent;
import javafx.scene.layout.BorderPane;
import javafx.application.Platform;


/** La fenêtre principale : assemble les composants et relie la liste au moteur. */
public class MainView extends BorderPane {

    public MainView(MoteurTorrent moteur) {
        ListeTorrents liste = new ListeTorrents();
        BarreEtat barreEtat = new BarreEtat(liste);

        setTop(new BarreAjout(moteur, barreEtat::afficher));
        setCenter(new TableTorrents(liste, moteur));
        setBottom(barreEtat);

        // Le moteur parle depuis son propre thread : on repasse sur le thread JavaFX
        moteur.surMiseAJour(etats -> Platform.runLater(() -> liste.appliquer(etats)));
        moteur.surSuppression(id -> Platform.runLater(() -> liste.retirer(id)));
    }
}
