package transactions.domain.model;

import java.util.Arrays;

public enum Operation {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal");

    private final String value;

    Operation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Operation from(String value) {
        return Arrays.stream(Operation.values())
                .filter(operation -> operation.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
