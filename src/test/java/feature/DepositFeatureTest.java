package feature;

import account.domain.model.Account;
import account.domain.port.AccountPort;
import account.infrastructure.adapter.AccountAdapter;
import account.infrastructure.mapper.AccountMapper;
import account.infrastructure.repository.InMemoryAccountRepository;
import account.use_case.Deposit;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import transactions.domain.model.Operation;
import transactions.domain.model.Transaction;
import transactions.domain.port.TransactionPort;
import transactions.infrastructure.adapter.TransactionAdapter;
import transactions.infrastructure.mapper.TransactionMapper;
import transactions.infrastructure.repository.InMemoryTransactionsHistoryRepository;
import user.domain.model.User;

import java.io.File;
import java.io.IOException;
import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DepositFeatureTest {
    private static final String ACCOUNT_MEMORY = "account.txt";
    private static final String TRANSACTION_FILE = "transacation.txt";

    private AccountMapper accountMapper;
    private TransactionMapper transactionMapper;
    private AccountPort accountPort;
    private TransactionPort transactionPort;

    private User user;

    public void cleanUpFiles() {
        File fileToDelete = new File(ACCOUNT_MEMORY);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
        fileToDelete = new File(TRANSACTION_FILE);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }

    private void initPorts(){
        InMemoryAccountRepository inMemoryAccountRepository = new InMemoryAccountRepository(ACCOUNT_MEMORY);
        InMemoryTransactionsHistoryRepository transactionsHistoryRepository = new InMemoryTransactionsHistoryRepository(TRANSACTION_FILE);
        accountMapper = new AccountMapper();
        transactionMapper = new TransactionMapper();
        accountPort = new AccountAdapter(inMemoryAccountRepository, accountMapper);
        transactionPort = new TransactionAdapter(transactionsHistoryRepository, transactionMapper);
    }

    @Given("user has {double} in his account")
    public void userHasInHisAccount(Double balance) throws IOException {
        initPorts();
        user = new User("1", "user");

        Account account = Account.AccountBuilder.builder().accountId("1").userId(user.getId()).balance(balance).build();
        accountPort.saveAccount(account);
    }

    @When("user makes a deposit of {double}")
    public void userDeposits(double amount) throws IOException {
        Clock clock = createClock();
        Transaction transaction = new Transaction("1", "1", Operation.DEPOSIT, LocalDate.now(clock), amount, 1000.00);

        Deposit deposit = new Deposit(accountPort, transactionPort);
        deposit.execute(user.id(), transaction);
    }

    @Then("user should have {double} in his account")
    public void userShouldHaveInHisAccount(Double balance) {
        Account account = accountPort.getAccountByUserId(user.getId());

        assertThat(balance).isEqualTo(account.getBalance());

        cleanUpFiles();
    }

    private Clock createClock() {
        return new Clock() {
            @Override
            public ZoneId getZone() {
                ZonedDateTime now = ZonedDateTime.of(2020, 12, 31, 0, 0, 0, 0, ZoneId.of("UTC"));
                return now.getZone();
            }
            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return Instant.now();
            }
        };
    }
}
