package page;

public class Boisson {
    private String nom;
    private double prix;
    private int quantite;

    // Constructeur
    public Boisson(String nom, double prix, int quantite) {
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        if (prix >= 0) {
            this.prix = prix;
        } else {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif.");
        }
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        if (quantite >= 0) {
            this.quantite = quantite;
        } else {
            throw new IllegalArgumentException("La quantité ne peut pas être négative.");
        }
    }

    @Override
    public String toString() {
        return "Boisson{" +
                "nom='" + nom + '\'' +
                ", prix=" + prix +
                ", quantite=" + quantite +
                '}';
    }
}