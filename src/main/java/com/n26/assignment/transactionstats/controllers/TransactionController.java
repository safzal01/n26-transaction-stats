package com.n26.assignment.transactionstats.controllers;

import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TransactionController.PATH)
public class TransactionController {
    protected final static String PATH = "/transactions";

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransaction(@RequestBody Transaction transaction) {
        transactionService.save(transaction);
    }
}
