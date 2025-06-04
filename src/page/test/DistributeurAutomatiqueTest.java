package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.Boisson;
import page.DistributeurAutomatique;
import page.Transaction;
import page.exeptions.BoissonNomTrouveeExeption;
import page.exeptions.MontantInsuffisantExeption;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DistributeurAutomatiqueTest {
    private DistributeurAutomatique distributeur;

    @BeforeEach
    void setUp() throws BoissonNomTrouveeExeption {
        // Initialisation du distributeur et ajout de boissons dans le stock
        distributeur = new DistributeurAutomatique();
        distributeur.rechargerStock("Cola", 10); // Prix par défaut : 1.0
        distributeur.rechargerStock("Eau", 5);   // Prix par défaut : 1.0
    }

    // Test 1 : Vérifier que consulterBoissonsDisponibles retourne la liste correcte
    @Test
    void testConsulterBoissonsDisponibles() {
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(2, boissons.size(), "Il devrait y avoir 2 boissons disponibles.");
        assertEquals("Cola", boissons.get(0).getNom(), "La première boisson devrait être 'Cola'.");
        assertEquals("Eau", boissons.get(1).getNom(), "La deuxième boisson devrait être 'Eau'.");
    }

    // Test 2 : Vérifier que consulterBoissonsDisponibles retourne une liste vide si aucun stock
    @Test
    void testConsulterBoissonsDisponiblesVide() throws BoissonNomTrouveeExeption {
        // Créer un nouveau distributeur sans stock
        DistributeurAutomatique distributeurVide = new DistributeurAutomatique();
        List<Boisson> boissons = distributeurVide.consulterBoissonsDisponibles();
        assertTrue(boissons.isEmpty(), "La liste devrait être vide si aucun stock.");
    }

    // Test 3 : Vérifier un achat réussi
    @Test
    void testAcheterBoissonSucces() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Cola", 2.0);
        assertEquals("Cola", transaction.getNomBoisson(), "La transaction devrait concerner 'Cola'.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix de la transaction devrait être 1.0.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0.");
        // Vérifier que la quantité a diminué
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(9, boissons.stream()
                .filter(b -> b.getNom().equals("Cola"))
                .findFirst().get().getQuantite(), "La quantité de Cola devrait être 9 après l'achat.");
    }

    // Test 4 : Vérifier un achat avec montant exact
    @Test
    void testAcheterBoissonMontantExact() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Eau", 1.0);
        assertEquals("Eau", transaction.getNomBoisson(), "La transaction devrait concerner 'Eau'.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix de la transaction devrait être 1.0.");
        assertEquals(1.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 1.0.");
    }

    // Test 5 : Vérifier une exception pour boisson non trouvée
    @Test
    void testAcheterBoissonNonTrouvee() {
        Exception exception = assertThrows(BoissonNomTrouveeExeption.class, () -> {
            distributeur.acheterBoisson("Jus", 2.0);
        });
        assertEquals("Boisson Jus non trouvée.", exception.getMessage(),
                "Une exception devrait être levée pour une boisson non trouvée.");
    }

    // Test 6 : Vérifier une exception pour rupture de stock
    @Test
    void testAcheterBoissonRuptureStock() throws BoissonNomTrouveeExeption {
        // Épuiser le stock de Cola
        for (int i = 0; i < 10; i++) {
            try {
                distributeur.acheterBoisson("Cola", 2.0);
            } catch (MontantInsuffisantExeption e) {
                fail("Montant insuffisant non attendu.");
            }
        }
        Exception exception = assertThrows(BoissonNomTrouveeExeption.class, () -> {
            distributeur.acheterBoisson("Cola", 2.0);
        });
        assertEquals("Boisson Cola en rupture de stock.", exception.getMessage(),
                "Une exception devrait être levée pour rupture de stock.");
    }

    // Test 7 : Vérifier une exception pour montant insuffisant
    @Test
    void testAcheterBoissonMontantInsuffisant() {
        Exception exception = assertThrows(MontantInsuffisantExeption.class, () -> {
            distributeur.acheterBoisson("Cola", 0.5);
        });
        assertEquals("Montant inséré (0.5) insuffisant pour Cola (1.0).", exception.getMessage(),
                "Une exception devrait être levée pour montant insuffisant.");
    }

    // Test 8 : Vérifier une recharge de stock réussie
    @Test
    void testRechargerStockSucces() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Cola", 5);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(15, boissons.stream()
                .filter(b -> b.getNom().equals("Cola"))
                .findFirst().get().getQuantite(), "La quantité de Cola devrait être 15 après recharge.");
    }

    // Test 9 : Vérifier une recharge avec une nouvelle boisson
    @Test
    void testRechargerNouvelleBoisson() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Jus", 3);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertTrue(boissons.stream().anyMatch(b -> b.getNom().equals("Jus") && b.getQuantite() == 3),
                "La nouvelle boisson 'Jus' devrait être ajoutée avec 3 unités.");
    }

    // Test 10 : Vérifier une exception pour quantité négative lors de la recharge
    @Test
    void testRechargerStockQuantiteNegative() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            distributeur.rechargerStock("Cola", -1);
        });
        assertEquals("La quantité à recharger ne peut pas être négative.", exception.getMessage(),
                "Une exception devrait être levée pour une quantité négative.");
    }

    // Test 11 : Vérifier une recharge avec quantité nulle
    @Test
    void testRechargerStockQuantiteNulle() throws BoissonNomTrouveeExeption {
        distributeur.rechargerStock("Cola", 0);
        List<Boisson> boissons = distributeur.consulterBoissonsDisponibles();
        assertEquals(10, boissons.stream()
                .filter(b -> b.getNom().equals("Cola"))
                .findFirst().get().getQuantite(), "La quantité de Cola devrait rester 10 après une recharge nulle.");
    }
}