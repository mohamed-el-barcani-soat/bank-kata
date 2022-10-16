package account.infrastructure.adapter;

import account.domain.model.Account;
import account.infrastructure.exception.SaveAccountUnsuccessfulException;
import account.infrastructure.mapper.AccountMapper;
import account.infrastructure.model.AccountEntity;
import account.infrastructure.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountAdapterTest {
    private AccountAdapter accountAdapter;
    private AccountMapper accountMapper;

    private InMemoryAccountRepository inMemoryAccountRepository;

    private final Account account = Account.AccountBuilder.builder()
            .accountId("1")
            .userId("1")
            .balance(1000.0)
            .build();
    private final AccountEntity accountEntity = new AccountEntity("1;1;1000");

    @BeforeEach
    void setUp() {
        inMemoryAccountRepository = mock(InMemoryAccountRepository.class);
        accountMapper = mock(AccountMapper.class);
        accountAdapter = new AccountAdapter(inMemoryAccountRepository, accountMapper);
    }

    @Test
    void when_get_account_by_user_should_call_in_memory_account_repository_read_account_by_user_id() {
        // Given
        when(inMemoryAccountRepository.readAccountByUserId("1")).thenReturn(accountEntity);

        // When
        accountAdapter.getAccountByUserId("1");

        // Then
        verify(inMemoryAccountRepository).readAccountByUserId("1");
    }

    @Test
    void when_get_account_by_user_should_return_correct_account() {
        // Given
        when(accountMapper.mapToDomain(accountEntity)).thenReturn(account);
        when(inMemoryAccountRepository.readAccountByUserId("1")).thenReturn(accountEntity);

        // When
        Account account = accountAdapter.getAccountByUserId("1");

        // Then
        assertThat(account.getAccountId()).isEqualTo("1");
        assertThat(account.getUserId()).isEqualTo("1");
        assertThat(account.getBalance()).isEqualTo(1000);
    }

    @Test
    void when_save_account_returns_bad_id_should_call_save_unsuccessful_exception() throws IOException {
        // Given
        when(accountMapper.mapToEntity(account)).thenReturn(accountEntity);
        when(inMemoryAccountRepository.writeInFile(accountEntity.getAccountData())).thenReturn(false);

        // When // Then
        var message = assertThrows(SaveAccountUnsuccessfulException.class, () -> accountAdapter.saveAccount(account));

        // Then
        assertThat(message.getMessage()).isEqualTo("Save account unsuccessful");
    }

    @Test
    void when_save_account_successes_should_return_true() throws IOException {
        // Given
        when(accountMapper.mapToEntity(account)).thenReturn(accountEntity);
        when(inMemoryAccountRepository.writeInFile(accountEntity.getAccountData())).thenReturn(true);

        // When // Then
        assertThat(accountAdapter.saveAccount(account)).isEqualTo(Optional.of("1"));
    }
}