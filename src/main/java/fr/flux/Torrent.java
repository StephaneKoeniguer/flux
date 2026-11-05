package fr.flux;

import javafx.beans.property.*;

public class Torrent {

    public enum Statut {
        TELECHARGEMENT("Téléchargement"),
        PAUSE("Pause"),
        PARTAGE("Partage"),
        TERMINE("Terminé");

        private final String libelle;

        Statut(String libelle) {
            this.libelle =  libelle;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private final StringProperty nom = new SimpleStringProperty();
    private final LongProperty taille = new SimpleLongProperty();
    private final DoubleProperty progression = new SimpleDoubleProperty();
    private final ObjectProperty<Statut> statut = new SimpleObjectProperty<>(Statut.TELECHARGEMENT);
    private final LongProperty vitesse = new SimpleLongProperty();

    public Torrent(String nom, long taille) {
        this.nom.set(nom);
        this.taille.set(taille);
    }

    // Convention JavaFX

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
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

    public void setProgression(double progression) {
        this.progression.set(progression);
    }

    public DoubleProperty progressionProperty() {
        return progression;
    }

    public Statut getStatut() {
        return statut.get();
    }

    public void setStatut(Statut statut) {
        this.statut.set(statut);
    }

    public ObjectProperty<Statut> statutProperty() {
        return statut;
    }

    public long getVitesse() {
        return vitesse.get();
    }

    public void setVitesse(long vitesse) {
        this.vitesse.set(vitesse);
    }


    public LongProperty vitesseProperty() {
        return vitesse;
    }

}
