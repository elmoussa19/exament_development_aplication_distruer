package page;

import java.time.LocalDateTime;

public class Transaction {
    private String nomBoisson;
    private double prix;
    private double montantInsere;
    private LocalDateTime date;

    // Constructeur
    public Transaction(String nomBoisson, double prix, double montantInsere) {
        this.nomBoisson = nomBoisson;
        this.prix = prix;
        this.montantInsere = montantInsere;
        this.date = LocalDateTime.now();
    }

    // Getters
    public String getNomBoisson() {
        return nomBoisson;
    }

    public double getPrix() {
        return prix;
    }

    public double getMontantInsere() {
        return montantInsere;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "nomBoisson='" + nomBoisson + '\'' +
                ", prix=" + prix +
                ", montantInsere=" + montantInsere +
                ", date=" + date +
                '}';
    }
}