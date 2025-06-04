package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.Portefeuille;

import static org.junit.jupiter.api.Assertions.*;

public class PortefeuilleTest {
    private Portefeuille portefeuille;

    @BeforeEach
    void setUp() {
        // Initialisation d'un nouveau Portefeuille avant chaque test
        portefeuille = new Portefeuille();
    }

    // Test 1 : Vérifier que le constructeur initialise le solde à 0
    @Test
    void testConstructeurSoldeInitial() {
        assertEquals(0.0, portefeuille.getSolde(), 0.001,
                "Le solde initial devrait être 0.0 après la création.");
    }

    // Test 2 : Vérifier l'ajout d'un montant positif
    @Test
    void testAjouterMontantPositif() {
        portefeuille.ajouterMontant(10.0);
        assertEquals(10.0, portefeuille.getSolde(), 0.001,
                "Le solde devrait être 10.0 après l'ajout d'un montant de 10.0.");
    }

    // Test 3 : Vérifier l'ajout de plusieurs montants positifs
    @Test
    void testAjouterPlusieursMontants() {
        portefeuille.ajouterMontant(5.0);
        portefeuille.ajouterMontant(3.5);
        assertEquals(8.5, portefeuille.getSolde(), 0.001,
                "Le solde devrait être 8.5 après l'ajout de 5.0 et 3.5.");
    }

    // Test 4 : Vérifier l'ajout d'un montant zéro
    @Test
    void testAjouterMontantZero() {
        portefeuille.ajouterMontant(0.0);
        assertEquals(0.0, portefeuille.getSolde(), 0.001,
                "Le solde devrait rester 0.0 après l'ajout d'un montant de 0.0.");
    }

    // Test 5 : Vérifier une exception pour un montant négatif
    @Test
    void testAjouterMontantNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.ajouterMontant(-1.0);
        });
        assertEquals("Le montant ne peut pas être négatif.", exception.getMessage(),
                "Une exception devrait être levée pour un montant négatif.");
    }

    // Test 6 : Vérifier la précision des calculs avec des montants décimaux
    @Test
    void testAjouterMontantDecimal() {
        portefeuille.ajouterMontant(1.99);
        portefeuille.ajouterMontant(0.01);
        assertEquals(2.0, portefeuille.getSolde(), 0.001,
                "Le solde devrait être 2.0 après l'ajout de 1.99 et 0.01.");
    }

    // Test 7 : Vérifier que getSolde retourne toujours la valeur correcte
    @Test
    void testGetSoldeApresMultiplesAjouts() {
        portefeuille.ajouterMontant(10.0);
        portefeuille.ajouterMontant(20.0);
        assertEquals(30.0, portefeuille.getSolde(), 0.001,
                "Le solde devrait être 30.0 après plusieurs ajouts.");
    }

    // Test 8 : Vérifier que le solde reste inchangé après une tentative d'ajout négatif
    @Test
    void testSoldeInchangeApresMontantNegatif() {
        portefeuille.ajouterMontant(5.0);
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.ajouterMontant(-2.0);
        });
        assertEquals(5.0, portefeuille.getSolde(), 0.001,
                "Le solde devrait rester 5.0 après une tentative d'ajout négatif.");
    }
}