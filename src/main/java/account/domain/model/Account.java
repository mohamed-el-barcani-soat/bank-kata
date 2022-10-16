package account.domain.model;

import account.domain.exception.InsufficientFundsException;

public class Account {
    private String accountId;
    private String userId;
    private Double balance;

    public void deposit(Double amount) {
        balance += amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void withdraw(Double amount) throws InsufficientFundsException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds in your account");
        }
        balance -= amount;
    }


    public static final class AccountBuilder {
        private String accountId;
        private String userId;
        private Double balance;

        private AccountBuilder() {
        }

        public static AccountBuilder builder() {
            return new AccountBuilder();
        }

        public AccountBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public AccountBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AccountBuilder balance(Double balance) {
            this.balance = balance;
            return this;
        }

        public Account build() {
            Account account = new Account();
            account.userId = this.userId;
            account.balance = this.balance;
            account.accountId = this.accountId;
            return account;
        }
    }
}
