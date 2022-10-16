package transactions.infrastructure.mapper;

import account.domain.model.Account;
import account.infrastructure.model.FileConstants;
import transactions.domain.model.Operation;
import transactions.domain.model.Transaction;
import transactions.infrastructure.model.TransactionEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static transactions.infrastructure.model.FileConstants.END_OF_LINE;
import static transactions.infrastructure.model.FileConstants.SEPARATOR;

public class TransactionMapper {
    public TransactionEntity mapToEntity(Transaction transaction) {
        StringBuilder transactionData = new StringBuilder();

        String formattedDate = getFormattedDate(transaction);
        transactionData.append(transaction.id()).append(SEPARATOR)
                .append(transaction.accountId()).append(SEPARATOR)
                .append(transaction.operation().getValue()).append(SEPARATOR)
                .append(formattedDate).append(SEPARATOR)
                .append(transaction.amount()).append(SEPARATOR)
                .append(transaction.balance()).append(END_OF_LINE);
        return new TransactionEntity(transactionData.toString());
    }

    private static String getFormattedDate(Transaction transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return transaction.date().format(formatter);
    }

    public Transaction mapToDomain(TransactionEntity transactionEntity) {
        String[] lineSplitted = transactionEntity.getTransactionData().split(FileConstants.SEPARATOR);

        LocalDate date = getParsedDate(lineSplitted[3]);
        return new Transaction(lineSplitted[0], lineSplitted[1], Operation.from(lineSplitted[2]), date, Double.parseDouble(lineSplitted[4]), Double.parseDouble(lineSplitted[5]));
    }

    private LocalDate getParsedDate(String lineSplitted) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(lineSplitted, formatter);
    }
}
