package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionHandler {
    static final Logger logger = LoggerFactory.getLogger(TransactionHandler.class);


    public void handleTransaction(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);
    }
}