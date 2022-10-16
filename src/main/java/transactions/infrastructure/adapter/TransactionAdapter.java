package transactions.infrastructure.adapter;

import transactions.domain.model.Transaction;
import transactions.domain.port.TransactionPort;
import transactions.infrastructure.mapper.TransactionMapper;
import transactions.infrastructure.repository.InMemoryTransactionsHistoryRepository;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TransactionAdapter implements TransactionPort {
    private final InMemoryTransactionsHistoryRepository transactionsHistoryRepository;

    private final TransactionMapper transactionMapper;

    public TransactionAdapter(InMemoryTransactionsHistoryRepository transactionsHistoryRepository, TransactionMapper transactionMapper) {
        this.transactionsHistoryRepository = transactionsHistoryRepository;
        this.transactionMapper = transactionMapper;
    }



    @Override
    public Optional<String> saveTransaction(Transaction transaction) throws IOException {
        var transactionsEntity = transactionMapper.mapToEntity(transaction);
        if(transactionsHistoryRepository.writeInFile(transactionsEntity.getTransactionData())){
            return Optional.of(transaction.id());
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> getTransactions(String accountId) {
        var transactionsEntity = transactionsHistoryRepository.findAllByAccountId(accountId);
        return transactionsEntity.stream().map(transactionMapper::mapToDomain).toList();
    }
}
