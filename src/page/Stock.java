package page;


import page.exeptions.BoissonNomTrouveeExeption;

import java.util.ArrayList;
import java.util.List;

public class Stock {
    private static List<Boisson> boissons;

    // Constructeur
    public Stock() {
        this.boissons = new ArrayList<>();
    }

    // Ajouter une boisson ou augmenter sa quantité
    public void ajouterBoisson(String nom, int quantite) throws BoissonNomTrouveeExeption {
        Boisson boisson = getBoissonParNom(nom);
        if (boisson != null) {
            boisson.setQuantite(boisson.getQuantite() + quantite);
        } else {
            // Si la boisson n'existe pas, créer une nouvelle (prix par défaut).
            boissons.add(new Boisson(nom, 1.0, quantite)); // Prix par défaut, à ajuster
        }
    }

    // Retirer une boisson (décrémenter la quantité)
    public void retirerBoisson(String nom) throws BoissonNomTrouveeExeption {
        Boisson boisson = getBoissonParNom(nom);
        if (boisson == null) {
            throw new BoissonNomTrouveeExeption("Boisson " + nom + " non trouvée.");
        }
        if (boisson.getQuantite() <= 0) {
            throw new BoissonNomTrouveeExeption("Boisson " + nom + " en rupture de stock.");
        }
        boisson.setQuantite(boisson.getQuantite() - 1);
    }

    // Obtenir une boisson par son nom
    public static Boisson getBoissonParNom(String nom) {
        for (Boisson boisson : boissons) {
            if (boisson.getNom().equalsIgnoreCase(nom)) {
                return boisson;
            }
        }
        return null;
    }

    // Obtenir les boissons disponibles (quantité > 0)
    public List<Boisson> getBoissonsDisponibles() {
        List<Boisson> disponibles = new ArrayList<>();
        for (Boisson boisson : boissons) {
            if (boisson.getQuantite() > 0) {
                disponibles.add(boisson);
            }
        }
        return disponibles;
    }
}