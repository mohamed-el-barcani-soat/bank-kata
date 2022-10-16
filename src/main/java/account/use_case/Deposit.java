package account.use_case;

import account.domain.model.Account;
import account.domain.port.AccountPort;
import account.infrastructure.exception.SaveAccountUnsuccessfulException;
import account.infrastructure.exception.SaveTransactionUnsuccessfulException;
import transactions.domain.model.Transaction;
import transactions.domain.port.TransactionPort;

import java.io.IOException;

public class Deposit {
    private final AccountPort accountPort;
    private final TransactionPort transactionPort;

    public Deposit(AccountPort accountPort, TransactionPort transactionPort) {
        this.accountPort = accountPort;
        this.transactionPort = transactionPort;
    }

    public String execute(String userId, Transaction transaction) throws IOException {
        Account account = accountPort.getAccountByUserId(userId);
        account.deposit(transaction.amount());
        var accountId = accountPort.saveAccount(account).orElseThrow(SaveAccountUnsuccessfulException::new);
        Transaction maybeTransaction = new Transaction(
                transaction.id(),
                accountId,
                transaction.operation(),
                transaction.date(),
                transaction.amount(),
                account.getBalance());
        return transactionPort.saveTransaction(maybeTransaction).orElseThrow(SaveTransactionUnsuccessfulException::new);
    }
}
