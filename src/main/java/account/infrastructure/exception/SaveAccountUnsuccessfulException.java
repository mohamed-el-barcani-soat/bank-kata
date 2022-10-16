package account.infrastructure.exception;

public class SaveAccountUnsuccessfulException extends RuntimeException {
    public SaveAccountUnsuccessfulException() {
        super("Save account unsuccessful");
    }
}
