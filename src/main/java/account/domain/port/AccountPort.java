package account.domain.port;

import account.domain.model.Account;

import java.io.IOException;
import java.util.Optional;

public interface AccountPort {
    Account getAccountByUserId(String userId);

    Optional<String> saveAccount(Account account) throws IOException;
}
