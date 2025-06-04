package page;

import java.util.ArrayList;
import java.util.List;

public class JournalVentes {
    private List<Transaction> transactions;

    // Constructeur
    public JournalVentes() {
        this.transactions = new ArrayList<>();
    }

    // Ajouter une transaction
    public void ajouterTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // Obtenir toutes les transactions
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}