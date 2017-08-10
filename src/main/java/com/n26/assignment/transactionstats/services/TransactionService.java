package com.n26.assignment.transactionstats.services;

import com.n26.assignment.transactionstats.models.Transaction;

public interface TransactionService {
    void save(Transaction transaction);
}
