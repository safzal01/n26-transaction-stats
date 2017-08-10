package com.n26.assignment.transactionstats.controllers;

import com.n26.assignment.transactionstats.services.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StatisticsController.class, secure = false)
public class StatisticsControllerTest {

    @MockBean
    private StatisticsService statisticsService;
    @Autowired
    private StatisticsController statisticsController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetStatisticsWithOkStatus() throws Exception {
        mockMvc.perform(get(StatisticsController.PATH))
                .andExpect(status().isOk());
    }
}