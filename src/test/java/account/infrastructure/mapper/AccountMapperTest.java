package account.infrastructure.mapper;

import account.domain.model.Account;
import account.infrastructure.mapper.AccountMapper;
import account.infrastructure.model.AccountEntity;
import account.infrastructure.model.FileConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountMapperTest {

    private AccountMapper accountMapper;
    @BeforeEach
    void setup() {
        accountMapper = new AccountMapper();
    }

    @Test
    void should_map_correctly_account_model_to_entity_model() {
        // Given
        Account account = Account.AccountBuilder.builder()
                .accountId("1")
                .userId("1")
                .balance(1000.0)
                .build();

        // When
        AccountEntity result = accountMapper.mapToEntity(account);

        // Then
        assertThat(result.getAccountData()).isEqualTo("1;1;1000.0"+ FileConstants.END_OF_LINE);
    }

    @Test
    void should_map_correctly_entity_model_to_model_model() {
        // Given
        AccountEntity accountEntity = new AccountEntity("1;1;1000.0");

        // When
        Account result = accountMapper.mapToDomain(accountEntity);

        // Then
        assertThat(result.getAccountId()).isEqualTo("1");
        assertThat(result.getUserId()).isEqualTo("1");
        assertThat(result.getBalance()).isEqualTo(1000.0);
    }
}