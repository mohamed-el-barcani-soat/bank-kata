package feature;

import account.domain.model.Account;
import account.domain.port.AccountPort;
import account.infrastructure.adapter.AccountAdapter;
import account.infrastructure.mapper.AccountMapper;
import account.infrastructure.repository.InMemoryAccountRepository;
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
import user.domain.model.User;

import java.io.File;
import java.io.IOException;
import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class WithdrawFeatureTest {
    private static final String ACCOUNT_MEMORY = "account.txt";

    private static final String TRANSACTION_FILE = "transaction.txt";

    private AccountMapper accountMapper;
    private TransactionMapper transactionMapper;

    private User user;

    private Throwable throwable;

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

    @Given("I have {double} in my account")
    public void iHaveInMyAccount(Double balance) throws IOException {
        user = new User("1", "user");

        Account account = Account.AccountBuilder.builder().accountId("1").userId(user.getId()).balance(balance).build();

        AccountPort accountPort = new AccountAdapter(new InMemoryAccountRepository(ACCOUNT_MEMORY), new AccountMapper());
        accountPort.saveAccount(account);
    }

    @When("I withdraw {double}")
    public void iWithdraw(Double amount) {
        InMemoryAccountRepository inMemoryAccountRepository = new InMemoryAccountRepository(ACCOUNT_MEMORY);
        InMemoryTransactionsHistoryRepository inMemoryTransactionsHistoryRepository = new InMemoryTransactionsHistoryRepository(TRANSACTION_FILE);
        accountMapper = new AccountMapper();
        transactionMapper = new TransactionMapper();

        ZonedDateTime now = ZonedDateTime.of(2020, 12, 31, 0, 0, 0, 0, ZoneId.of("UTC"));
        Clock clock = createClock();
        AccountPort accountPort = new AccountAdapter(inMemoryAccountRepository, accountMapper);
        TransactionPort transactionPort = new TransactionAdapter(inMemoryTransactionsHistoryRepository, transactionMapper);

        Transaction transaction = new Transaction("1", "1", Operation.DEPOSIT, LocalDate.now(clock), amount, 1000.00);

        Withdraw withdraw = new Withdraw(accountPort, transactionPort);
        throwable = catchThrowable(() -> withdraw.execute(user.getId(), transaction));
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

    @Then("I should have {double} in my account")
    public void iShouldHaveInMyAccount(Double balance) {
        InMemoryAccountRepository inMemoryAccountRepository = new InMemoryAccountRepository(ACCOUNT_MEMORY);
        AccountPort accountPort = new AccountAdapter(inMemoryAccountRepository, accountMapper);
        Account account = accountPort.getAccountByUserId(user.getId());

        assertThat(balance).isEqualTo(account.getBalance());
    }

    @And("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String message) {
        assertThat(throwable).isNotNull();
        assertThat(message).isEqualTo(throwable.getMessage());

        cleanUpFiles();
    }
}
