package account.infrastructure.mapper;

import account.domain.model.Account;
import account.infrastructure.model.AccountEntity;

import static account.infrastructure.model.FileConstants.END_OF_LINE;
import static account.infrastructure.model.FileConstants.SEPARATOR;

public class AccountMapper {

    public AccountEntity mapToEntity(Account account) {
        String accountData = account.getAccountId() + SEPARATOR +
                account.getUserId() + SEPARATOR +
                account.getBalance() + END_OF_LINE;
        return new AccountEntity(accountData);
    }

    public Account mapToDomain(AccountEntity accountEntity) {
        String[] lineSplitted = accountEntity.getAccountData().split(SEPARATOR);
        return Account.AccountBuilder.builder()
                .accountId(lineSplitted[0])
                .userId(lineSplitted[1])
                .balance(Double.parseDouble(lineSplitted[2].trim())).build();
    }
}
