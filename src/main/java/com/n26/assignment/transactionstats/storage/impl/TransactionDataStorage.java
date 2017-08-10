package com.n26.assignment.transactionstats.storage.impl;

import com.n26.assignment.transactionstats.models.Statistics;
import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.storage.DataStorage;
import com.n26.assignment.transactionstats.util.TimeUtil;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionDataStorage implements DataStorage<Transaction, Collection<Statistics>> {

    private static final ConcurrentHashMap<Integer, Statistics> storage = new ConcurrentHashMap(60);

    /*
    * save only last minute transactions
    * each Entry in storage Map will hold statistics for one second
    * if second is not in current minute system will replace it with default statistics( containing zero values)
    * */


    @Override
    public void save(Transaction transaction) {
        long currentTimestamp = System.currentTimeMillis();
        int second = TimeUtil.getSecondFromTimeStamp(transaction.getTimestamp());
        storage.compute(second, (k, v) -> this.computeStatisticsForSecond(transaction, k, v, currentTimestamp));
    }

    private Statistics computeStatisticsForSecond(Transaction transaction, Integer key, Statistics value, long currentTimestamp) {
        if (value == null || TimeUtil.isTimeOlderThanSixtySeconds(currentTimestamp, value.getTimestamp())) {
            return mapTransactionToStatistics(transaction);
        }
        value.setCount(value.getCount() + 1);
        value.setSum(value.getSum() + transaction.getAmount());
        value.setMin(Math.min(value.getMin(), transaction.getAmount()));
        value.setMax(Math.max(value.getMax(), transaction.getAmount()));
        return value;
    }

    private Statistics mapTransactionToStatistics(Transaction transaction) {
        return Statistics.builder()
                .timestamp(transaction.getTimestamp())
                .count(1L)
                .sum(transaction.getAmount())
                .max(transaction.getAmount())
                .min(transaction.getAmount())
                .build();
    }

    @Override
    public Collection<Statistics> fetchStatistics() {
        return Collections.unmodifiableCollection(storage.values());
    }
}
