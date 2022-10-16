package account.infrastructure.adapter;

import account.domain.model.Account;
import account.domain.port.AccountPort;
import account.infrastructure.exception.SaveAccountUnsuccessfulException;
import account.infrastructure.mapper.AccountMapper;
import account.infrastructure.repository.InMemoryAccountRepository;

import java.io.IOException;
import java.util.Optional;

public class AccountAdapter implements AccountPort {

    private final InMemoryAccountRepository inMemoryAccountRepository;
    private final AccountMapper accountMapper;

    public AccountAdapter(InMemoryAccountRepository inMemoryAccountRepository, AccountMapper accountMapper) {
        this.inMemoryAccountRepository = inMemoryAccountRepository;
        this.accountMapper = accountMapper;
    }


    @Override
    public Account getAccountByUserId(String userId) {
        var accountEntity = inMemoryAccountRepository.readAccountByUserId(userId);
        return accountMapper.mapToDomain(accountEntity);
    }

    @Override
    public Optional<String> saveAccount(Account account) throws IOException {
        var accountEntity = accountMapper.mapToEntity(account);
        if (inMemoryAccountRepository.writeInFile(accountEntity.getAccountData())) {
            return Optional.of(account.getAccountId());
        }
        throw new SaveAccountUnsuccessfulException();
    }
}
