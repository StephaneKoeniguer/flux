package fr.flux.moteur;


public enum Statut {
    RECHERCHE("Recherche des infos"),
    VERIFICATION("Vérification"),
    TELECHARGEMENT("Téléchargement"),
    PAUSE("En pause"),
    PARTAGE("En partage"),
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


