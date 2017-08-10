package com.n26.assignment.transactionstats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Transaction is stale")
public class StaleTransactionException extends RuntimeException {

    public StaleTransactionException(String message) {
        super(message);
    }
}
