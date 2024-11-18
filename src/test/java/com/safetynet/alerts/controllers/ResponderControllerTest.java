package com.safetynet.alerts.controllers;

import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.services.ResponderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ResponderController.class)
@Import(DataRepo.class)
public class ResponderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResponderService responderService;

    @Test
    public void testGetFireStationPeople() throws Exception {
        // Arrange: Mock data
        int testStationNumber = 1;

        mockMvc.perform(get("/api/firestation")
                        .param("stationNumber", String.valueOf(testStationNumber))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
        //TODO imporve expectations of the test as no.of people, no of adults & no of children.
        //

    }
}
