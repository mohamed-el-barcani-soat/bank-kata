package transactions.infrastructure.repository;

import transactions.infrastructure.model.TransactionEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.List;

import static account.infrastructure.model.FileConstants.SEPARATOR;

public class InMemoryTransactionsHistoryRepository {
    private final String fileName;


    public InMemoryTransactionsHistoryRepository(String file_name) {
        this.fileName = file_name;
    }

    public boolean writeInFile(String content) throws IOException {
        RandomAccessFile fileWriter = new RandomAccessFile(fileName, "rw");
        FileChannel channel = fileWriter.getChannel();
        FileLock lock;
        try {
            lock = channel.tryLock();
        } catch (final OverlappingFileLockException e) {
            fileWriter.close();
            channel.close();
            return false;
        }
        fileWriter.seek(fileWriter.length());
        fileWriter.write(content.getBytes());
        lock.release();

        fileWriter.close();
        channel.close();
        return true;
    }

    public List<TransactionEntity> findAllByAccountId(String accountId) {
        List<TransactionEntity> transactions = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null){
                if(lineContains(line, accountId)) {
                    transactions.add(new TransactionEntity(line));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private boolean lineContains(String line, String accountId) {
        String[] lineSplitted = line.split(SEPARATOR);
        return !line.isEmpty() && lineSplitted[1].equals(accountId);
    }
}
