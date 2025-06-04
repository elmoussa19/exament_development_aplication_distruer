package page;

public class Portefeuille {
    private double solde;

    // Constructeur
    public Portefeuille() {
        this.solde = 0.0;
    }

    // Ajouter un montant au portefeuille
    public void ajouterMontant(double montant) {
        if (montant >= 0) {
            this.solde += montant;
        } else {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif.");
        }
    }

    // Obtenir le solde
    public double getSolde() {
        return solde;
    }
}