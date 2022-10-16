package transactions.domain.model;

import java.time.LocalDate;

public record Transaction(String id,
                          String accountId,
                          Operation operation,
                          LocalDate date,
                          Double amount,
                          Double balance) {

}
