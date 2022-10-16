package account.infrastructure.exception;

public class SaveTransactionUnsuccessfulException extends RuntimeException {
    public SaveTransactionUnsuccessfulException() {
        super("Transaction not saved");
    }
}
