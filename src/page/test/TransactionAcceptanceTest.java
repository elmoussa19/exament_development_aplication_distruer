package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.Boisson;
import page.DistributeurAutomatique;
import page.Transaction;
import page.exeptions.BoissonNomTrouveeExeption;
import page.exeptions.MontantInsuffisantExeption;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionAcceptanceTest {
    private DistributeurAutomatique distributeur;

    @BeforeEach
    void setUp() throws BoissonNomTrouveeExeption {
        distributeur = new DistributeurAutomatique();
        // Ajouter des boissons avec des prix spécifiques
        distributeur.rechargerStock("Cola", 5);
        distributeur.getBoissonParNom("Cola").setPrix(1.0);
        distributeur.rechargerStock("Eau", 10);
        distributeur.getBoissonParNom("Eau").setPrix(0.5);
        distributeur.rechargerStock("Café Long", 5);
        distributeur.getBoissonParNom("Café Long").setPrix(1.5);
        distributeur.rechargerStock("Thé", 5);
        distributeur.getBoissonParNom("Thé").setPrix(1.25);
    }

    // Scénario 1
    @Test
    void testTransactionAchatMontantSuffisant() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Cola", 2.0);
        assertEquals("Cola", transaction.getNomBoisson(), "La transaction concerne Cola.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix devrait être 1.0€.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0€.");
    }

    // Scénario 2
    @Test
    void testTransactionAchatMontantExact() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Eau", 0.5);
        assertEquals("Eau", transaction.getNomBoisson(), "La transaction concerne Eau.");
        assertEquals(0.5, transaction.getPrix(), 0.001, "Le prix devrait être 0.5€.");
        assertEquals(0.5, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 0.5€.");
    }

    // Scénario 3
    @Test
    void testTransactionDateRecente() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Cola", 2.0);
        LocalDateTime now = LocalDateTime.now();
        assertTrue(transaction.getDate().isAfter(now.minusSeconds(5)),
                "La date de la transaction devrait être récente (moins de 5 secondes).");
    }

    // Scénario 4
    @Test
    void testTransactionEnregistreeJournal() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        distributeur.acheterBoisson("Cola", 2.0);
        List<Transaction> transactions = distributeur.getJournalVentes();
        assertEquals(1, transactions.size(), "Une transaction devrait être enregistrée.");
        Transaction transaction = transactions.get(0);
        assertEquals("Cola", transaction.getNomBoisson(), "La transaction concerne Cola.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix devrait être 1.0€.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0€.");
    }

    // Scénario 5
    @Test
    void testPlusieursTransactionsJournal() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        distributeur.acheterBoisson("Cola", 2.0);
        distributeur.acheterBoisson("Eau", 1.0);
        List<Transaction> transactions = distributeur.getJournalVentes();
        assertEquals(2, transactions.size(), "Deux transactions devraient être enregistrées.");
        assertTrue(transactions.stream().anyMatch(t -> t.getNomBoisson().equals("Cola") && t.getPrix() == 1.0 && t.getMontantInsere() == 2.0),
                "Une transaction pour Cola devrait être présente.");
        assertTrue(transactions.stream().anyMatch(t -> t.getNomBoisson().equals("Eau") && t.getPrix() == 0.5 && t.getMontantInsere() == 1.0),
                "Une transaction pour Eau devrait être présente.");
    }

    // Scénario 6
    @Test
    void testAucuneTransactionMontantInsuffisant() {
        assertThrows(MontantInsuffisantExeption.class, () -> {
            distributeur.acheterBoisson("Cola", 0.5);
        });
        List<Transaction> transactions = distributeur.getJournalVentes();
        assertTrue(transactions.isEmpty(), "Aucune transaction ne devrait être enregistrée.");
    }

    // Scénario 7
    @Test
    void testAucuneTransactionBoissonNonExistante() {
        assertThrows(BoissonNomTrouveeExeption.class, () -> {
            distributeur.acheterBoisson("Jus", 2.0);
        });
        List<Transaction> transactions = distributeur.getJournalVentes();
        assertTrue(transactions.isEmpty(), "Aucune transaction ne devrait être enregistrée.");
    }

    // Scénario 8
    @Test
    void testAucuneTransactionRuptureStock() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        for (int i = 0; i < 5; i++) {
            distributeur.acheterBoisson("Cola", 2.0);
        }
        assertThrows(BoissonNomTrouveeExeption.class, () -> {
            distributeur.acheterBoisson("Cola", 2.0);
        });
        List<Transaction> transactions = distributeur.getJournalVentes();
        assertEquals(5, transactions.size(), "Seules les 5 transactions initiales devraient être enregistrées.");
    }

    // Scénario 9
    @Test
    void testTransactionNomBoissonComplexe() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Café Long", 2.0);
        assertEquals("Café Long", transaction.getNomBoisson(), "La transaction concerne Café Long.");
        assertEquals(1.5, transaction.getPrix(), 0.001, "Le prix devrait être 1.5€.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0€.");
    }

    // Scénario 10
    @Test
    void testTransactionPrixDecimalPrecis() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Thé", 2.0);
        assertEquals("Thé", transaction.getNomBoisson(), "La transaction concerne Thé.");
        assertEquals(1.25, transaction.getPrix(), 0.001, "Le prix devrait être 1.25€.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0€.");
    }

    // Scénario 11
    @Test
    void testTransactionMontantInsereEleve() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Cola", 10.0);
        assertEquals("Cola", transaction.getNomBoisson(), "La transaction concerne Cola.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix devrait être 1.0€.");
        assertEquals(10.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 10.0€.");
    }

    // Scénario 12
    @Test
    void testRepresentationTextuelleTransaction() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        Transaction transaction = distributeur.acheterBoisson("Eau", 1.0);
        String toString = transaction.toString();
        assertTrue(toString.contains("nomBoisson='Eau'"), "La représentation devrait contenir le nom Eau.");
        assertTrue(toString.contains("prix=0.5"), "La représentation devrait contenir le prix 0.5.");
        assertTrue(toString.contains("montantInsere=1.0"), "La représentation devrait contenir le montant inséré 1.0.");
    }



    // Scénario 14
    @Test
    void testTransactionApresRechargeStock() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        DistributeurAutomatique distributeurLocal = new DistributeurAutomatique();
        distributeurLocal.rechargerStock("Cola", 1);
        distributeurLocal.getBoissonParNom("Cola").setPrix(1.0);
        distributeurLocal.acheterBoisson("Cola", 2.0);
        distributeurLocal.rechargerStock("Cola", 5);
        Transaction transaction = distributeurLocal.acheterBoisson("Cola", 2.0);
        assertEquals("Cola", transaction.getNomBoisson(), "La transaction concerne Cola.");
        assertEquals(1.0, transaction.getPrix(), 0.001, "Le prix devrait être 1.0€.");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0€.");
    }

    // Scénario 15
    @Test
    void testOrdreTransactionsJournal() throws BoissonNomTrouveeExeption, MontantInsuffisantExeption {
        distributeur.acheterBoisson("Cola", 2.0);
        distributeur.acheterBoisson("Eau", 1.0);
        List<Transaction> transactions = distributeur.getJournalVentes();
        assertEquals(2, transactions.size(), "Deux transactions devraient être enregistrées.");
        assertEquals("Cola", transactions.get(0).getNomBoisson(), "La première transaction concerne Cola.");
        assertEquals("Eau", transactions.get(1).getNomBoisson(), "La deuxième transaction concerne Eau.");
    }
}