package com.n26.assignment.transactionstats.services.impl;

import com.n26.assignment.transactionstats.exception.InvalidTransactionException;
import com.n26.assignment.transactionstats.exception.StaleTransactionException;
import com.n26.assignment.transactionstats.models.Statistics;
import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.storage.DataStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private DataStorage<Transaction, Collection<Statistics>> transactionCollectionDataStorage;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test(expected = InvalidTransactionException.class)
    public void shouldThrowExceptionWhenTransactionIsNull(){
        transactionService.save(null);
    }

    @Test(expected = InvalidTransactionException.class)
    public void shouldThrowExceptionWhenInvalidTimestampInTransaction(){
        transactionService.save(Transaction.builder().timestamp(-1).build());
    }

    @Test(expected = StaleTransactionException.class)
    public void shouldThrowExceptionWhenTransactionIsOlderThanSixtySeconds(){
        transactionService.save(Transaction.builder().timestamp(System.currentTimeMillis() - 70000).build());
    }

    @Test
    public void shouldSaveFreshTransaction(){
        doNothing().when(transactionCollectionDataStorage).save(any(Transaction.class));
        transactionService.save(Transaction.builder().timestamp(System.currentTimeMillis() - 4000).build());
        verify(transactionCollectionDataStorage, times(1)).save(any(Transaction.class));
    }
}