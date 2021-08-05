package com.paurus.betting.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class TaxationRestTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Incoming incoming1;
    private Outgoing outgoing1;
    private Incoming incoming2;
    private Outgoing outgoing2;
    private Incoming incoming5;

    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        incoming1 = Incoming.builder()
                .traderId(1)
                .playedAmount(5)
                .odd(1.5)
                .build();
        outgoing1 = Outgoing.builder()
                .taxRate(0.1)
                .possibleReturnAmount(7.5)
                .possibleReturnAmountBefTax(7.5)
                .possibleReturnAmountAfterTax(6.75)
                .build();

        incoming2 = Incoming.builder()
                .traderId(2)
                .playedAmount(8)
                .odd(3.2)
                .build();
        outgoing2 = Outgoing.builder()
                .taxAmount(1.0)
                .possibleReturnAmount(7.5)
                .possibleReturnAmountBefTax(7.5)
                .possibleReturnAmountAfterTax(6.75)
                .build();

        incoming5 = Incoming.builder()
                .traderId(5)
                .playedAmount(8)
                .odd(3.2)
                .build();
    }

    @Test
    public void generalTaxationByRate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/taxation/general/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(incoming1))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.possibleReturnAmountAfterTax").value(6.75));
    }

    @Test
    public void generalWinningsByAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/taxation/winnings/amount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(incoming2))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxAmount").value(1.0));
    }

    @Test
    public void generalWinningsByAmountError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/taxation/winnings/amount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(incoming5))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Tax amount for this trader not found!"));
    }
}
