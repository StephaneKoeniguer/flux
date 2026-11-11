package fr.flux.model;

import fr.flux.moteur.EtatTorrent;
import fr.flux.moteur.Statut;
import javafx.beans.property.*;

public class Torrent {

    private final String id;
    private final StringProperty nom = new SimpleStringProperty();
    private final LongProperty taille = new SimpleLongProperty();
    private final DoubleProperty progression = new SimpleDoubleProperty();
    private final ObjectProperty<Statut> statut = new SimpleObjectProperty<>(Statut.TELECHARGEMENT);
    private final LongProperty vitesse = new SimpleLongProperty();
    private final LongProperty vitesseMontante = new SimpleLongProperty();
    private final IntegerProperty pairs = new SimpleIntegerProperty();

    public Torrent(String id) {
        this.id = id;
    }

    public void mettreAJour(EtatTorrent etat) {
        nom.set(etat.nom());
        taille.set(etat.taille());
        progression.set(etat.progression());
        statut.set(etat.statut());
        vitesse.set(etat.vitesseDescendante());
        vitesseMontante.set(etat.vitesseMontante());
        pairs.set(etat.pairs());
    }

    public String getId() {
        return this.id;
    }

    public String getNom() {
        return nom.get();
    }
    public StringProperty nomProperty() {
        return nom;
    }

    public long getTaille() {
        return taille.get();
    }
    public LongProperty tailleProperty() {
        return taille;
    }

    public double getProgression() {
        return progression.get();
    }
    public DoubleProperty progressionProperty() {
        return progression;
    }

    public Statut getStatut() {
        return statut.get();
    }
    public ObjectProperty<Statut> statutProperty() {
        return statut;
    }

    public long getVitesse() {return vitesse.get();}
    public LongProperty vitesseProperty() {
        return vitesse;
    }

    public long getVitesseMontante() {return vitesseMontante.get();}
    public LongProperty vitesseMontanteProperty() {return vitesseMontante;}

    public int getPairs() {return pairs.get();}
    public IntegerProperty pairsProperty() {return pairs;}

}
