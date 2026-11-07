package fr.flux;

public record EtatTorrent(
        String id,
        String nom,
        long taille,
        double progression,
        Statut statut,
        long vitesseDescendante,
        long vitesseMontante)
{}
