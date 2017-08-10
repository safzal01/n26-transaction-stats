package com.n26.assignment.transactionstats.services.impl;

import com.n26.assignment.transactionstats.models.Statistics;
import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.services.StatisticsService;
import com.n26.assignment.transactionstats.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LastMinuteStatisticsService implements StatisticsService {
    private static final int MILLIS_IN_ONE_SECOND = 1000;
    private static final int SECONDS_IN_ONE_MINUTE = 60;

    private DataStorage<Transaction, Collection<Statistics>> transactionCollectionDataStorage;

    @Autowired
    LastMinuteStatisticsService(DataStorage<Transaction, Collection<Statistics>> transactionCollectionDataStorage) {
        this.transactionCollectionDataStorage = transactionCollectionDataStorage;
    }

    /*
    *  Get summary; always deals with 60 statistics so it is O(1)
    * */

    @Override
    public Statistics get() {
        long currentTimeStamp = System.currentTimeMillis();
        Collection<Statistics> statisticsCollection = transactionCollectionDataStorage.fetchStatistics();
        return calculateStatisticsSummaryForLastSixtySeconds(statisticsCollection, currentTimeStamp);
    }

    private Statistics calculateStatisticsSummaryForLastSixtySeconds(Collection<Statistics> statisticsCollection, long currentTimeStamp) {
        Statistics statisticsSummary = statisticsCollection.stream()
                .filter(statistics -> this.isStatisticsForCurrentMinute(statistics, currentTimeStamp))
                .reduce(Statistics.builder().min(Double.MAX_VALUE).build(), this::reduction);
        if (statisticsSummary.getCount() > 0) {
            statisticsSummary.setAvg(statisticsSummary.getSum() / statisticsSummary.getCount());
        }
        if(Double.compare(statisticsSummary.getMin(), Double.MAX_VALUE) == 0){
            statisticsSummary.setMin(0);
        }
        return statisticsSummary;
    }

    private boolean isStatisticsForCurrentMinute(Statistics statistics, long currentTimeStamp) {
        return (currentTimeStamp - statistics.getTimestamp()) / MILLIS_IN_ONE_SECOND < SECONDS_IN_ONE_MINUTE;
    }

    private Statistics reduction(Statistics statistics1, Statistics statistics2) {
        return Statistics.builder()
                .count(statistics1.getCount() + statistics2.getCount())
                .sum(statistics1.getSum() + statistics2.getSum())
                .min(Math.min(statistics1.getMin(), statistics2.getMin()))
                .max(Math.max(statistics1.getMax(), statistics2.getMax()))
                .build();
    }
}
