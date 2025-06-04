package page;

import page.exeptions.BoissonNomTrouveeExeption;
import page.exeptions.MontantInsuffisantExeption;

class Main {
    public static void main(String[] args) {
        DistributeurAutomatique distributeur = new DistributeurAutomatique();

        try {
            // Ajouter des boissons au stock
            distributeur.rechargerStock("Cola", 10);
            distributeur.rechargerStock("Eau", 15);

            // Consulter les boissons disponibles
            System.out.println("Boissons disponibles : " + distributeur.consulterBoissonsDisponibles());

            // Effectuer un achat
            Transaction transaction = distributeur.acheterBoisson("Cola", 2.0);
            System.out.println("Achat effectué : " + transaction);

        } catch (BoissonNomTrouveeExeption | MontantInsuffisantExeption e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}