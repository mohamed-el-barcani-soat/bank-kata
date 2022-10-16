package transactions.use_case;

import account.domain.port.AccountPort;
import transactions.domain.model.Transaction;
import transactions.domain.port.TransactionPort;

import java.util.List;

public class ShowHistory {

    private final TransactionPort transactionPort;
    private final AccountPort accountPort;
    private final String accountId;

    public ShowHistory(TransactionPort transactionPort, AccountPort accountPort, String accountId) {
        this.transactionPort = transactionPort;
        this.accountPort = accountPort;
        this.accountId = accountId;
    }

    public List<Transaction> execute() {
        return transactionPort.getTransactions(accountId);
    }
}
