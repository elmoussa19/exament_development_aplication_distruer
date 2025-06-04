package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.Boisson;
import page.Stock;
import page.exeptions.BoissonNomTrouveeExeption;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {
    private Stock stock;

    @BeforeEach
    void setUp() {
        // Initialisation d'un nouveau Stock avant chaque test
        stock = new Stock();
    }

    // Test 1 : Vérifier que le constructeur initialise une liste vide
    @Test
    void testConstructeurListeVide() {
        List<Boisson> boissons = stock.getBoissonsDisponibles();
        assertTrue(boissons.isEmpty(), "La liste des boissons devrait être vide après la création.");
    }

    // Test 2 : Vérifier l'ajout d'une nouvelle boisson
    @Test
    void testAjouterNouvelleBoisson() throws BoissonNomTrouveeExeption {
        stock.ajouterBoisson("Cola", 10);
        List<Boisson> boissons = stock.getBoissonsDisponibles();
        assertEquals(1, boissons.size(), "La liste devrait contenir une boisson.");
        assertEquals("Cola", boissons.get(0).getNom(), "La boisson ajoutée devrait être 'Cola'.");
        assertEquals(10, boissons.get(0).getQuantite(), "La quantité devrait être 10.");
        assertEquals(1.0, boissons.get(0).getPrix(), 0.001, "Le prix par défaut devrait être 1.0.");
    }

    // Test 3 : Vérifier l'augmentation de la quantité d'une boisson existante
    @Test
    void testAjouterBoissonExistante() throws BoissonNomTrouveeExeption {
        stock.ajouterBoisson("Cola", 5);
        stock.ajouterBoisson("Cola", 3);
        List<Boisson> boissons = stock.getBoissonsDisponibles();
        assertEquals(1, boissons.size(), "La liste devrait contenir une seule boisson.");
        assertEquals(8, boissons.get(0).getQuantite(), "La quantité de Cola devrait être 8 (5+3).");
    }



    // Test 5 : Vérifier le retrait d'une boisson
    @Test
    void testRetirerBoisson() throws BoissonNomTrouveeExeption {
        stock.ajouterBoisson("Cola", 5);
        stock.retirerBoisson("Cola");
        List<Boisson> boissons = stock.getBoissonsDisponibles();
        assertEquals(4, boissons.get(0).getQuantite(), "La quantité de Cola devrait être 4 après retrait.");
    }

    // Test 6 : Vérifier une exception pour retirer une boisson non trouvée
    @Test
    void testRetirerBoissonNonTrouvee() {
        Exception exception = assertThrows(BoissonNomTrouveeExeption.class, () -> {
            stock.retirerBoisson("Jus");
        });
        assertEquals("Boisson Jus non trouvée.", exception.getMessage(),
                "Une exception devrait être levée pour une boisson non trouvée.");
    }

    // Test 7 : Vérifier une exception pour retirer une boisson en rupture de stock
    @Test
    void testRetirerBoissonRuptureStock() throws BoissonNomTrouveeExeption {
        stock.ajouterBoisson("Cola", 1);
        stock.retirerBoisson("Cola"); // Quantité passe à 0
        Exception exception = assertThrows(BoissonNomTrouveeExeption.class, () -> {
            stock.retirerBoisson("Cola");
        });
        assertEquals("Boisson Cola en rupture de stock.", exception.getMessage(),
                "Une exception devrait être levée pour une boisson en rupture de stock.");
    }

    // Test 8 : Vérifier getBoissonParNom avec une boisson existante
    @Test
    void testGetBoissonParNomExistante() throws BoissonNomTrouveeExeption {
        stock.ajouterBoisson("Cola", 5);
        Boisson boisson = stock.getBoissonParNom("Cola");
        assertNotNull(boisson, "La boisson devrait être trouvée.");
        assertEquals("Cola", boisson.getNom(), "Le nom de la boisson devrait être 'Cola'.");
        assertEquals(5, boisson.getQuantite(), "La quantité devrait être 5.");
    }

    // Test 9 : Vérifier getBoissonParNom avec une boisson inexistante
    @Test
    void testGetBoissonParNomInexistante() {
        Boisson boisson = stock.getBoissonParNom("Jus");
        assertNull(boisson, "La méthode devrait retourner null pour une boisson inexistante.");
    }

    // Test 10 : Vérifier getBoissonsDisponibles avec des boissons épuisées
    @Test
    void testGetBoissonsDisponiblesAvecEpuisees() throws BoissonNomTrouveeExeption {
        stock.ajouterBoisson("Cola", 1);
        stock.ajouterBoisson("Eau", 0); // Quantité 0
        stock.retirerBoisson("Cola"); // Épuise Cola
        List<Boisson> boissons = stock.getBoissonsDisponibles();
        assertTrue(boissons.isEmpty(), "La liste devrait être vide si toutes les boissons sont épuisées.");
    }
}