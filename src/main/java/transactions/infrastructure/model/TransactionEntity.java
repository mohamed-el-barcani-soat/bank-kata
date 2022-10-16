package transactions.infrastructure.model;

public class TransactionEntity {
    private final String transactionData;

    public TransactionEntity(String transactionData) {
        this.transactionData = transactionData;
    }

    public String getTransactionData() {
        return transactionData;
    }
}
