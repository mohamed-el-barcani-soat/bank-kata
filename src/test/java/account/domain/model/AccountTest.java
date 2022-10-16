package account.domain.model;

import account.domain.exception.InsufficientFundsException;
import account.domain.model.Account;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void when_deposit_new_balance_should_be_greater_than_old_balance() {
        // Given
        Account account = Account.AccountBuilder.builder()
                .accountId("1")
                .userId("1")
                .balance(1000.0)
                .build();

        // When
        account.deposit(100.0);

        // Then
        assertThat(account.getBalance()).isGreaterThan(1000.0);
    }

    @Test
    void when_deposit_new_balance_should_be_less_than_old_balance() {
        // Given
        Account account = Account.AccountBuilder.builder()
                .accountId("1")
                .userId("1")
                .balance(1000.0)
                .build();

        // When
        account.withdraw(100.0);

        // Then
        assertThat(account.getBalance()).isLessThan(1000.0);
    }

    @Test
    void when_insufficient_funds_should_throw_exception() {
        // Given
        Account account = Account.AccountBuilder.builder()
                .accountId("1")
                .userId("1")
                .balance(1000.0)
                .build();

        // When
        var exception = assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(1000.1));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Insufficient funds in your account");
    }
}