package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.Boisson;

import static org.junit.jupiter.api.Assertions.*;

public class BoissonTest {
    private Boisson boisson;

    @BeforeEach
    void setUp() {
        // Initialisation d'une boisson avant chaque test
        boisson = new Boisson("Cola", 1.5, 10);
    }

    // Test 1 : Vérifier que le constructeur initialise correctement le nom
    @Test
    void testConstructeurNom() {
        assertEquals("Cola", boisson.getNom(), "Le nom de la boisson devrait être 'Cola'.");
    }

    // Test 2 : Vérifier que le constructeur initialise correctement le prix
    @Test
    void testConstructeurPrix() {
        assertEquals(1.5, boisson.getPrix(), 0.001, "Le prix de la boisson devrait être 1.5.");
    }

    // Test 3 : Vérifier que le constructeur initialise correctement la quantité
    @Test
    void testConstructeurQuantite() {
        assertEquals(10, boisson.getQuantite(), "La quantité de la boisson devrait être 10.");
    }

    // Test 4 : Vérifier que setNom fonctionne correctement
    @Test
    void testSetNom() {
        boisson.setNom("Eau");
        assertEquals("Eau", boisson.getNom(), "Le nom de la boisson devrait être mis à jour à 'Eau'.");
    }

    // Test 5 : Vérifier que setPrix fonctionne avec un prix valide
    @Test
    void testSetPrixValide() {
        boisson.setPrix(2.0);
        assertEquals(2.0, boisson.getPrix(), 0.001, "Le prix devrait être mis à jour à 2.0.");
    }

    // Test 6 : Vérifier que setPrix lance une exception pour un prix négatif
    @Test
    void testSetPrixNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boisson.setPrix(-1.0);
        });
        assertEquals("Le prix ne peut pas être négatif.", exception.getMessage(),
                "Une exception devrait être levée pour un prix négatif.");
    }

    // Test 7 : Vérifier que setQuantite fonctionne avec une quantité valide
    @Test
    void testSetQuantiteValide() {
        boisson.setQuantite(5);
        assertEquals(5, boisson.getQuantite(), "La quantité devrait être mis à jour à 5.");
    }

    // Test 8 : Vérifier que setQuantite lance une exception pour une quantité négative
    @Test
    void testSetQuantiteNegative() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boisson.setQuantite(-1);
        });
        assertEquals("La quantité ne peut pas être négative.", exception.getMessage(),
                "Une exception devrait être levée pour une quantité négative.");
    }

    // Test 9 : Vérifier le cas limite où le prix est 0
    @Test
    void testSetPrixZero() {
        boisson.setPrix(0.0);
        assertEquals(0.0, boisson.getPrix(), 0.001, "Le prix devrait être mis à jour à 0.0.");
    }

    // Test 10 : Vérifier le cas limite où la quantité est 0
    @Test
    void testSetQuantiteZero() {
        boisson.setQuantite(0);
        assertEquals(0, boisson.getQuantite(), "La quantité devrait être mis à jour à 0.");
    }

    // Test 11 : Vérifier la méthode toString
    @Test
    void testToString() {
        String expected = "Boisson{nom='Cola', prix=1.5, quantite=10}";
        assertEquals(expected, boisson.toString(), "La méthode toString devrait retourner la représentation correcte.");
    }
}