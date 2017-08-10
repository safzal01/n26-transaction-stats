package com.n26.assignment.transactionstats.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.assignment.transactionstats.exception.StaleTransactionException;
import com.n26.assignment.transactionstats.models.Transaction;
import com.n26.assignment.transactionstats.services.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TransactionController.class, secure = false)
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void shouldSaveFreshTransactionWithCreatedStatus() throws Exception {
        mockMvc.perform(post(TransactionController.PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createTransaction(true))))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnProperStatusForStaleTransaction() throws Exception {
        doThrow(StaleTransactionException.class).when(transactionService).save(any(Transaction.class));

        mockMvc.perform(post(TransactionController.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTransaction(false))))
                .andExpect(status().isNoContent());
    }


    private Transaction createTransaction(boolean isFresh){
        long currentTimestamp = System.currentTimeMillis();
        return Transaction.builder().amount(12.0).timestamp(isFresh ? currentTimestamp - 1000 : currentTimestamp - 70000 ).build();
    }
}