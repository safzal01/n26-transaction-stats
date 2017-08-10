package com.n26.assignment.transactionstats.services.impl;

import com.n26.assignment.transactionstats.exception.InvalidTransactionException;
import com.n26.assignment.transactionstats.exception.StaleTransactionException;
import com.n26.assignment.transactionstats.models.Statistics;
import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.services.TransactionService;
import com.n26.assignment.transactionstats.storage.DataStorage;
import com.n26.assignment.transactionstats.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private DataStorage<Transaction, Collection<Statistics>> dataStorage;

    TransactionServiceImpl(DataStorage<Transaction, Collection<Statistics>> dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public void save(Transaction transaction) {
        log.debug("Going to save transaction");
        validateTransaction(transaction);
        dataStorage.save(transaction);
        log.debug("Transaction is saved successfully");
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction == null || transaction.getTimestamp() < 1) {
            log.error("##Invalid Transaction## Transaction is null or invalid timestamp");
            throw new InvalidTransactionException("Transaction is null or invalid timestamp");
        }
        if (TimeUtil.isTimeOlderThanSixtySeconds(System.currentTimeMillis(), transaction.getTimestamp())) {
            log.error("##Invalid Transaction## Transaction is stale");
            throw new StaleTransactionException("Transaction is stale");
        }
    }
}
