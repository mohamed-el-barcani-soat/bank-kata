package transactions.domain.port;

import transactions.domain.model.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TransactionPort {
    Optional<String> saveTransaction(Transaction transaction) throws IOException;

    List<Transaction> getTransactions(String accountId);
}
