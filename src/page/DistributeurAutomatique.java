package page;

import page.exeptions.BoissonNomTrouveeExeption;
import page.exeptions.MontantInsuffisantExeption;

import java.util.ArrayList;
import java.util.List;

public class DistributeurAutomatique {
    private Stock stock;
    private Portefeuille portefeuille;
    private JournalVentes journalVentes;

    // Constructeur
    public DistributeurAutomatique() {
        this.stock = new Stock();
        this.portefeuille = new Portefeuille();
        this.journalVentes = new JournalVentes();
    }

    // Consulter les boissons disponibles
    public List<Boisson> consulterBoissonsDisponibles() {
        return stock.getBoissonsDisponibles();
    }

    // Acheter une boisson
    public Transaction acheterBoisson(String nomBoisson, double montantInsere)
            throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Boisson boisson = stock.getBoissonParNom(nomBoisson);
        if (boisson == null) {
            throw new BoissonNomTrouveeExeption("Boisson " + nomBoisson + " non trouvée.");
        }
        if (boisson.getQuantite() <= 0) {
            throw new BoissonNomTrouveeExeption("Boisson " + nomBoisson + " en rupture de stock.");
        }
        if (montantInsere < boisson.getPrix()) {
            throw new MontantInsuffisantExeption("Montant inséré (" + montantInsere + ") insuffisant pour " + nomBoisson + " (" + boisson.getPrix() + ").");
        }

        stock.retirerBoisson(nomBoisson);
        Transaction transaction = new Transaction(nomBoisson, boisson.getPrix(), montantInsere);
        journalVentes.ajouterTransaction(transaction);
        portefeuille.ajouterMontant(boisson.getPrix());
        return transaction;
    }

    // Recharger le stock
    public void rechargerStock(String nomBoisson, int quantite) throws BoissonNomTrouveeExeption {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité à recharger ne peut pas être négative.");
        }
        stock.ajouterBoisson(nomBoisson, quantite);
    }

    // Getter pour les tests d’acceptance
    public List<Transaction> getJournalVentes() {
        return journalVentes.getTransactions();
    }

    public Boisson getBoissonParNom(String nomBoisson) {
        return stock.getBoissonParNom(nomBoisson);
    }
}