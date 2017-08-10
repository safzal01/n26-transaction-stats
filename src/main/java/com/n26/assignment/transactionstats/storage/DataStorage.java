package com.n26.assignment.transactionstats.storage;

public interface DataStorage<T, R> {
    void save(T t);
    R fetchStatistics();
}
