package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.Boisson;
import page.DistributeurAutomatique;
import page.Transaction;
import page.exeptions.BoissonNomTrouveeExeption;
import page.exeptions.MontantInsuffisantExeption;
import page.Stock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DistributeurAutomatiqueAcceptanceTest {
    private DistributeurAutomatique distributeur;

    @BeforeEach
    void setUp() throws BoissonNomTrouveeExeption {
        distributeur = new DistributeurAutomatique();
        // Ajouter des boissons avec des prix spécifiques
        distributeur.rechargerStock("Cola", 5);
        Stock.getBoissonParNom("Cola").setPrix(1.0);
        distributeur.rechargerStock("Eau", 10);
        Stock.getBoissonParNom("Eau").setPrix(0.5);
    }

    // Scénario 1
    @Test
    void testConsulterBoissonsAvecStockInitial() {
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(2, boissons.size(), "La liste devrait contenir 2 boissons.");
        assertTrue(boissons.stream().anyMatch(b -> b.getNom().equals("Cola") && b.getQuantite() == 5 && b.getPrix() == 1.0),
                "Cola devrait être présent avec 5 unités et prix 1.0€.");
        assertTrue(boissons.stream().anyMatch(b -> b.getNom().equals("Eau") && b.getQuantite() == 10 && b.getPrix() == 0.5),
                "Eau devrait être présent avec 10 unités et prix 0.5€.");
    }

    // Scénario 2
    @Test
    void testConsulterBoissonsStockVide() {
        DistributeurAutomatique distributeurVide = new DistributeurAutomatique();
        List<Boisson> boissons = distributeurVide.consulterBoissonsDisponibles();
        assertTrue(boissons.isEmpty(), "La liste devrait être vide.");
    }

    // Scénario 3
    @Test
    void testAcheterBoissonMontantSuffisant() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Cola", 2.0);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(4, boissons.stream().filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait être 4.");
        assertEquals("Cola", transaction.getNomBoisson(), "La transaction concerne Cola.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix devrait être 1.0€.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0€.");
    }

    // Scénario 4
    @Test
    void testAcheterBoissonMontantExact() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Eau", 0.5);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(9, boissons.stream().filter(b -> b.getNom().equals("Eau")).findFirst().get().getQuantite(),
                "La quantité d'Eau devrait être 9.");
        assertEquals("Eau", transaction.getNomBoisson(), "La transaction concerne Eau.");
        assertEquals(0.5, transaction.getPrix(), 0.001, "Le prix devrait être 0.5€.");
        assertEquals(0.5, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 0.5€.");
    }

    // Scénario 5
    @Test
    void testAcheterBoissonMontantInsuffisant() {
        Exception exception = assertThrows(MontantInsuffisantExeption.class, () -> {
            distributeur.acheterBoisson("Cola", 0.5);
        });
        assertEquals("Montant inséré (0.5) insuffisant pour Cola (1.0).", exception.getMessage(),
                "Une erreur de montant insuffisant devrait être affichée.");
        assertEquals(5, distributeur.consulterBoissonsDisponibles().stream()
                        .filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait rester 5.");
    }

    // Scénario 6
    @Test
    void testAcheterBoissonNonExistante() {
        Exception exception = assertThrows(BoissonNomTrouveeExeption.class, () -> {
            distributeur.acheterBoisson("Jus", 2.0);
        });
        assertEquals("Boisson Jus non trouvée.", exception.getMessage(),
                "Une erreur de boisson non trouvée devrait être affichée.");
        assertEquals(5, distributeur.consulterBoissonsDisponibles().stream()
                        .filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait rester 5.");
    }

    // Scénario 7
    @Test
    void testAcheterBoissonRuptureStock() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        for (int i = 0; i < 5; i++) {
            distributeur.acheterBoisson("Cola", 2.0);
        }
        Exception exception = assertThrows(BoissonNomTrouveeExeption.class, () -> {
            distributeur.acheterBoisson("Cola", 2.0);
        });
        assertEquals("Boisson Cola en rupture de stock.", exception.getMessage(),
                "Une erreur de rupture de stock devrait être affichée.");
    }

    // Scénario 8
    @Test
    void testRechargerStockBoissonExistante() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Cola", 3);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(8, boissons.stream().filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait être 8.");
    }

    // Scénario 9
    @Test
    void testRechargerStockNouvelleBoisson() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Jus", 10);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertTrue(boissons.stream().anyMatch(b -> b.getNom().equals("Jus") && b.getQuantite() == 10 && b.getPrix() == 1.0),
                "Jus devrait être ajouté avec 10 unités et prix 1.0€.");
    }

    // Scénario 10
    @Test
    void testRechargerStockQuantiteNulle() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Cola", 0);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(5, boissons.stream().filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait rester 5.");
    }

    // Scénario 11
    @Test
    void testRechargerStockQuantiteNegative() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            distributeur.rechargerStock("Cola", -3);
        });
        assertEquals("La quantité à recharger ne peut pas être négative.", exception.getMessage(),
                "Une erreur de quantité négative devrait être affichée.");
        assertEquals(5, distributeur.consulterBoissonsDisponibles().stream()
                        .filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait rester 5.");
    }

    // Scénario 12
    @Test
    void testAcheterPlusieursBoissonsConsecutivement() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        distributeur.acheterBoisson("Cola", 2.0);
        distributeur.acheterBoisson("Eau", 1.0);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(4, boissons.stream().filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait être 4.");
        assertEquals(9, boissons.stream().filter(b -> b.getNom().equals("Eau")).findFirst().get().getQuantite(),
                "La quantité d'Eau devrait être 9.");
        assertEquals(2, distributeur.getJournalVentes().size(), "Deux transactions devraient être enregistrées.");
    }

    // Scénario 13
    @Test
    void testConsulterBoissonsApresAchat() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        distributeur.acheterBoisson("Cola", 2.0);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(4, boissons.stream().filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait être 4.");
        assertEquals(10, boissons.stream().filter(b -> b.getNom().equals("Eau")).findFirst().get().getQuantite(),
                "La quantité d'Eau devrait être 10.");
    }

    // Scénario 14
    @Test
    void testConsulterBoissonsApresRecharge() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Cola", 5);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(10, boissons.stream().filter(b -> b.getNom().equals("Cola")).findFirst().get().getQuantite(),
                "La quantité de Cola devrait être 10.");
    }


}