package account.use_case;

import account.domain.model.Account;
import account.domain.port.AccountPort;
import account.infrastructure.exception.SaveAccountUnsuccessfulException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transactions.domain.model.Operation;
import transactions.domain.model.Transaction;
import transactions.domain.port.TransactionPort;

import java.io.IOException;
import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DepositTest {

    private AccountPort accountPort;
    private TransactionPort transactionPort;
    private Clock clock;
    private Transaction transaction;
    private Account account;


    @BeforeEach
    void setup(){
        accountPort = mock(AccountPort.class);
        transactionPort = mock(TransactionPort.class);
        clock = createClock();
        transaction = new Transaction("1", "1", Operation.DEPOSIT, LocalDate.now(clock), 1000.0, 1000.00);
        account = Account.AccountBuilder.builder().accountId("1").userId("1").balance(1000.00).build();
    }

    @Test
    void when_bad_transaction_id_should_throw_unsuccessful_exception() throws IOException {
        when(accountPort.getAccountByUserId("1")).thenReturn(account);
        when(transactionPort.saveTransaction(transaction)).thenReturn(Optional.empty());

        Deposit deposit = new Deposit(accountPort, transactionPort);
        var message = assertThrows(SaveAccountUnsuccessfulException.class, () -> deposit.execute("1", transaction));
        assertThat(message.getMessage()).isEqualTo("Save account unsuccessful");
    }

    @Test
    void when_execute_deposit_should_call_account_adapter_get_account_by_user_id() throws IOException {
        // Given

        when(accountPort.getAccountByUserId("1")).thenReturn(account);
        when(accountPort.saveAccount(account)).thenReturn(Optional.of("1"));
        when(transactionPort.saveTransaction(any())).thenReturn(Optional.of("1"));

        var deposit = new Deposit(accountPort, transactionPort);

        // When
        var transactionId = deposit.execute("1", transaction);

        // Then

        assertThat(transactionId).isEqualTo("1");

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