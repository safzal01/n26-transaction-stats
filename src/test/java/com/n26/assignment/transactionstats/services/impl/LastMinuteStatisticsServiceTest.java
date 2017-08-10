package com.n26.assignment.transactionstats.services.impl;

import com.n26.assignment.transactionstats.models.Statistics;
import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.storage.DataStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LastMinuteStatisticsServiceTest {

    @Mock
    private DataStorage<Transaction, Collection<Statistics>> transactionCollectionDataStorage;
    @InjectMocks
    private LastMinuteStatisticsService lastMinuteStatisticsService;

    @Test
    public void shouldReturnEmptyStatisticsWhenThereIsNoTransactionInDataStore() {
        when(transactionCollectionDataStorage.fetchStatistics()).thenReturn(new ArrayList<>());

        Statistics result = lastMinuteStatisticsService.get();

        assertNotNull(result);
        assertEquals(0, result.getCount());
        assertEquals(0, result.getAvg(), 0);
        assertEquals(0, result.getMin(), 0);
        assertEquals(0, result.getMax(), 0);
        assertEquals(0, result.getSum(), 0);
    }

    @Test
    public void shouldReturnEmptyStatisticsWhenSixtySecondStatsInDataStoreAreState() {
        long currentTimestamp = System.currentTimeMillis();
        when(transactionCollectionDataStorage.fetchStatistics()).thenReturn(
                Arrays.asList(
                        Statistics.builder().timestamp(currentTimestamp - 70000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 80000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 90000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 100000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build()
                ));

        Statistics result = lastMinuteStatisticsService.get();

        assertNotNull(result);
        assertEquals(0, result.getCount());
        assertEquals(0, result.getAvg(), 0);
        assertEquals(0, result.getMin(), 0);
        assertEquals(0, result.getMax(), 0);
        assertEquals(0, result.getSum(), 0);
    }


    @Test
    public void shouldReturnStatisticsWhenSixtySecondStatsInDataStoreAreFresh() {
        long currentTimestamp = System.currentTimeMillis();
        when(transactionCollectionDataStorage.fetchStatistics()).thenReturn(
                Arrays.asList(
                        Statistics.builder().timestamp(currentTimestamp - 1000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 2000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 3000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 40000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build()
                ));

        Statistics result = lastMinuteStatisticsService.get();

        assertNotNull(result);
        assertEquals(8, result.getCount());
        assertEquals(6.0, result.getAvg(), 0);
        assertEquals(2.0, result.getMin(), 0);
        assertEquals(10.0, result.getMax(), 0);
        assertEquals(48.0, result.getSum(), 0);
    }

    @Test
    public void shouldReturnStatisticsWhenSixtySecondStatsInDataStoreAreMixOfFreshAndStale() {
        long currentTimestamp = System.currentTimeMillis();
        when(transactionCollectionDataStorage.fetchStatistics()).thenReturn(
                Arrays.asList(
                        Statistics.builder().timestamp(currentTimestamp - 1000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 2000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 3000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 100000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build(),
                        Statistics.builder().timestamp(currentTimestamp - 40000).count(2L).max(10.0).min(2.0).avg(6.0).sum(12.0).build()
                ));

        Statistics result = lastMinuteStatisticsService.get();

        assertNotNull(result);
        assertEquals(8, result.getCount());
        assertEquals(6.0, result.getAvg(), 0);
        assertEquals(2.0, result.getMin(), 0);
        assertEquals(10.0, result.getMax(), 0);
        assertEquals(48.0, result.getSum(), 0);
    }
}