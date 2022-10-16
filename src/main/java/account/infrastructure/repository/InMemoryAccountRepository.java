package account.infrastructure.repository;

import account.infrastructure.model.AccountEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import static account.infrastructure.model.FileConstants.SEPARATOR;

public class InMemoryAccountRepository {

    private final String fileName;


    public InMemoryAccountRepository(String file_name) {
        this.fileName = file_name;
    }

    public boolean writeInFile(String content) throws IOException {
        RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
        FileChannel channel = stream.getChannel();
        FileLock lock;
        try {
            lock = channel.tryLock();
        } catch (final OverlappingFileLockException e) {
            stream.close();
            channel.close();
            return false;
        }

        stream.write(content.getBytes());
        lock.release();

        stream.close();
        channel.close();
        return true;
    }

    public AccountEntity readAccountByUserId(String userId) {
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null && !lineContains(line, userId)) {
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AccountEntity(line);
    }

    private boolean lineContains(String line, String userId) {
        String[] lineSplitted = line.split(SEPARATOR);
        return lineSplitted[1].equals(userId);
    }
}
