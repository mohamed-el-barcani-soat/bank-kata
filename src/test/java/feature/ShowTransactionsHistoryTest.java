package feature;

import account.domain.model.Account;
import account.domain.port.AccountPort;
import account.infrastructure.adapter.AccountAdapter;
import account.infrastructure.mapper.AccountMapper;
import account.infrastructure.repository.InMemoryAccountRepository;
import account.use_case.Deposit;
import account.use_case.Withdraw;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import transactions.domain.model.Operation;
import transactions.domain.model.Transaction;
import transactions.domain.port.TransactionPort;
import transactions.infrastructure.adapter.TransactionAdapter;
import transactions.infrastructure.mapper.TransactionMapper;
import transactions.infrastructure.repository.InMemoryTransactionsHistoryRepository;
import transactions.use_case.ShowHistory;
import user.domain.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ShowTransactionsHistoryTest {
    private static final String ACCOUNT_MEMORY = "account.txt";
    private static final String TRANSACTION_MEMORY = "transaction.txt";

    private AccountPort accountPort;

    private TransactionPort transactionPort;

    private AccountMapper accountMapper;

    private Account account;
    private User user;

    private List<Transaction> transactions;

    public void cleanUpFiles() {
        File fileToDelete = new File(TRANSACTION_MEMORY);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
        fileToDelete = new File(ACCOUNT_MEMORY);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }

    private void initPorts(){
        accountMapper = new AccountMapper();
        accountPort = new AccountAdapter(new InMemoryAccountRepository(ACCOUNT_MEMORY), accountMapper);
        transactionPort = new TransactionAdapter(new InMemoryTransactionsHistoryRepository(TRANSACTION_MEMORY), new TransactionMapper());
    }

    @Given("I have {double} on my account")
    public void iHaveOnMyAccount(Double balance) throws IOException {
        initPorts();
        user = new User("1", "user");

        account = Account.AccountBuilder.builder().accountId("1").userId(user.getId()).balance(balance).build();
        accountPort.saveAccount(account);
    }

    @And("I have made a deposit of {double} on {int},{int},{int}")
    public void iHaveMadeADepositOfOn(Double amount, int day, int month, int year) throws IOException {
        Deposit deposit = new Deposit(accountPort, transactionPort);
        String transactionId = day + "-" + month + "-" + year; // in order to be unique at least for this test
        Transaction transaction = new Transaction(transactionId, account.getAccountId(), Operation.DEPOSIT, LocalDate.of(year, month, day), amount, account.getBalance());

        deposit.execute(user.id(), transaction);
    }

    @And("I have made a withdrawal of {double} on {int},{int},{int}")
    public void iHaveMadeAWithdrawalOfOn(Double amount, int day, int month, int year) throws IOException {
        Withdraw withdraw = new Withdraw(accountPort, transactionPort);
        String transactionId = day + "-" + month + "-" + year; // in order to be unique at least for this test
        Transaction transaction = new Transaction(transactionId, account.getAccountId(), Operation.WITHDRAWAL, LocalDate.of(year, month, day), amount, account.getBalance());

        withdraw.execute(user.id(), transaction);
    }

    @When("I show history")
    public void iShowHistory() {
        ShowHistory showHistory = new ShowHistory(transactionPort, accountPort, account.getAccountId());
        transactions = showHistory.execute();
    }

    @Then("I should have {int} transactions in my history")
    public void iShouldHaveTransactionsInMyHistory(int numberOfTransactions) {
        assertThat(transactions).hasSize(numberOfTransactions);
    }

    @And("for every {int}-{int}-{int} I would see a transaction containing exactly a {int}\\/{int}\\/{int}, a type of operation {double} or {double} and its amount, And the {double}")
    public void forEveryTransactionIdIWouldSeeATransactionListedByDateCreditDebitBalance(Integer idPart1, Integer idPart2, Integer idPart3, Integer day, Integer month, Integer year, Double credit, Double debit, Double balance) {
        Operation operation = credit > 0 ? Operation.DEPOSIT : Operation.WITHDRAWAL;
        transactions.stream().filter(t -> t.id().equals(idPart1 + "-" + idPart2 + "-" + idPart3)).forEach(t -> {
            assertThat(t.date()).isEqualTo(LocalDate.of(year, month, day));
            assertThat(t.operation()).isEqualTo(operation);
            assertThat(t.balance()).isEqualTo(balance);
        });
        LocalDate.of(year, month, day);
        cleanUpFiles();
    }
}
