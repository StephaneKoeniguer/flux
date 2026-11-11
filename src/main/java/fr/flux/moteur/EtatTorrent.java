package fr.flux.moteur;

public record EtatTorrent(
        String id,
        String nom,
        long taille,
        double progression,
        Statut statut,
        long vitesseDescendante,
        long vitesseMontante,
        int pairs
) {}
