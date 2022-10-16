package account.infrastructure.model;

public class AccountEntity {
    private final String accountData;

    public AccountEntity(String accountData) {
        this.accountData = accountData;
    }

    public String getAccountData() {
        return accountData;
    }
}
