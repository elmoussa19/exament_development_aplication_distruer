package page.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.JournalVentes;
import page.Transaction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JournalVentesTest {
    private JournalVentes journalVentes;

    @BeforeEach
    void setUp() {
        // Initialisation d'un nouveau JournalVentes avant chaque test
        journalVentes = new JournalVentes();
    }

    // Test 1 : Vérifier que le constructeur initialise une liste vide
    @Test
    void testConstructeurListeVide() {
        List<Transaction> transactions = journalVentes.getTransactions();
        assertTrue(transactions.isEmpty(), "La liste des transactions devrait être vide après la création.");
    }

    // Test 2 : Vérifier l'ajout d'une transaction
    @Test
    void testAjouterTransactionUnique() {
        Transaction transaction = new Transaction("Cola", 1.0, 2.0);
        journalVentes.ajouterTransaction(transaction);
        List<Transaction> transactions = journalVentes.getTransactions();
        assertEquals(1, transactions.size(), "La liste devrait contenir une transaction.");
        assertEquals(transaction, transactions.get(0), "La transaction ajoutée devrait être présente dans la liste.");
    }

    // Test 3 : Vérifier l'ajout de plusieurs transactions
    @Test
    void testAjouterPlusieursTransactions() {
        Transaction transaction1 = new Transaction("Cola", 1.0, 2.0);
        Transaction transaction2 = new Transaction("Eau", 0.5, 1.0);
        journalVentes.ajouterTransaction(transaction1);
        journalVentes.ajouterTransaction(transaction2);
        List<Transaction> transactions = journalVentes.getTransactions();
        assertEquals(2, transactions.size(), "La liste devrait contenir deux transactions.");
        assertTrue(transactions.contains(transaction1), "La première transaction devrait être présente.");
        assertTrue(transactions.contains(transaction2), "La deuxième transaction devrait être présente.");
    }

    // Test 4 : Vérifier que getTransactions retourne une copie de la liste
    @Test
    void testGetTransactionsRetourneCopie() {
        Transaction transaction = new Transaction("Cola", 1.0, 2.0);
        journalVentes.ajouterTransaction(transaction);
        List<Transaction> transactions = journalVentes.getTransactions();
        // Modifier la liste retournée
        transactions.clear();
        // Vérifier que la liste interne n'est pas affectée
        assertEquals(1, journalVentes.getTransactions().size(),
                "La liste interne ne devrait pas être modifiée par la modification de la copie.");
    }

    // Test 5 : Vérifier l'ajout d'une transaction null
    @Test
    void testAjouterTransactionNull() {
        journalVentes.ajouterTransaction(null);
        List<Transaction> transactions = journalVentes.getTransactions();
        assertEquals(1, transactions.size(), "Une transaction null devrait être ajoutée à la liste.");
        assertNull(transactions.get(0), "La transaction ajoutée devrait être null.");
    }

    // Test 6 : Vérifier que getTransactions retourne une liste vide après création
    @Test
    void testGetTransactionsApresCreation() {
        List<Transaction> transactions = journalVentes.getTransactions();
        assertNotNull(transactions, "La liste retournée ne devrait pas être null.");
        assertTrue(transactions.isEmpty(), "La liste devrait être vide juste après la création.");
    }

    // Test 7 : Vérifier l'ordre des transactions (FIFO)
    @Test
    void testOrdreTransactions() {
        Transaction transaction1 = new Transaction("Cola", 1.0, 2.0);
        Transaction transaction2 = new Transaction("Eau", 0.5, 1.0);
        journalVentes.ajouterTransaction(transaction1);
        journalVentes.ajouterTransaction(transaction2);
        List<Transaction> transactions = journalVentes.getTransactions();
        assertEquals(transaction1, transactions.get(0), "La première transaction ajoutée devrait être en position 0.");
        assertEquals(transaction2, transactions.get(1), "La deuxième transaction ajoutée devrait être en position 1.");
    }

    // Test 8 : Vérifier l'ajout de transactions identiques
    @Test
    void testAjouterTransactionsIdentiques() {
        Transaction transaction = new Transaction("Cola", 1.0, 2.0);
        journalVentes.ajouterTransaction(transaction);
        journalVentes.ajouterTransaction(transaction);
        List<Transaction> transactions = journalVentes.getTransactions();
        assertEquals(2, transactions.size(), "La liste devrait contenir deux transactions, même si elles sont identiques.");
        assertEquals(transaction, transactions.get(0), "La première transaction devrait être correcte.");
        assertEquals(transaction, transactions.get(1), "La deuxième transaction devrait être correcte.");
    }
}