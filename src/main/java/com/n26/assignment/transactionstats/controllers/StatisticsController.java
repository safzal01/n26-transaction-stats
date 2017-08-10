package com.n26.assignment.transactionstats.controllers;

import com.n26.assignment.transactionstats.models.Statistics;
import com.n26.assignment.transactionstats.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(StatisticsController.PATH)
public class StatisticsController {
    protected static final String PATH = "/statistics";

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public Statistics getLastSixtySecondTransactionStatistics() {
        return statisticsService.get();
    }
}
