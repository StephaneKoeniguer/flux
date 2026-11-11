package fr.flux.model;

import fr.flux.moteur.EtatTorrent;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** La liste des torrents, tenue à jour à partir des infos du moteur. */
public class ListeTorrents {

    private final ObservableList<Torrent> torrents = FXCollections.observableArrayList();
    private final ObservableList<Torrent> torrentsLectureSeule = FXCollections.unmodifiableObservableList(torrents);
    private final Map<String, Torrent> torrentsParId = new HashMap<>();
    private final ReadOnlyLongWrapper debitDescendant = new ReadOnlyLongWrapper();
    private final ReadOnlyLongWrapper debitMontant = new ReadOnlyLongWrapper();

    public void appliquer(List<EtatTorrent> etats) {
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

    public void retirer(String id) {
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
        debitDescendant.set(totalDescendant);
        debitMontant.set(totalMontant);
    }

    /** La liste observable, en lecture seule */
    public ObservableList<Torrent> getTorrents() { return torrentsLectureSeule; }

    public long getDebitDescendant() { return debitDescendant.get(); }

    public ReadOnlyLongProperty debitDescendantProperty() { return debitDescendant.getReadOnlyProperty(); }

    public long getDebitMontant() { return debitMontant.get(); }

    public ReadOnlyLongProperty debitMontantProperty() { return debitMontant.getReadOnlyProperty(); }

}
