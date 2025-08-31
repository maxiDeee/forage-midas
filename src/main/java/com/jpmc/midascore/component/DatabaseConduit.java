package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(Transaction transaction) {
        UserRecord sender = queryUser(transaction.getSenderId());
        UserRecord recipient = queryUser(transaction.getRecipientId());
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
        transactionRecordRepository.save(transactionRecord);


        sender.setBalance(sender.getBalance() - transaction.getAmount());
        save(sender);
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());
        save(recipient);
    }

    public boolean isValid(Transaction transaction) {
        UserRecord sender = queryUser(transaction.getSenderId());
        if (sender == null) {
            return false;
        }
        UserRecord recipient = queryUser(transaction.getRecipientId());
        if (recipient == null) {
            return false;
        }
        if (sender.getBalance() < transaction.getAmount()) {
            return false;
        }
        return true;
    }

    public UserRecord queryUser(long id) {
        return userRepository.findById(id);
    }

    public float queryUserBalance(long id) {
        UserRecord userRecord = queryUser(id);
        if (userRecord == null) {
            return -1;
        }
        return userRecord.getBalance();
    }
}
