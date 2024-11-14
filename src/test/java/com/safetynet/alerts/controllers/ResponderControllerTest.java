package com.safetynet.alerts.controllers;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResponderController.class)
@Import(DataRepo.class)
public class ResponderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetFireStationPeople() throws Exception {
        // Arrange: Mock data
        int testStationNumber = 1;

        MockHttpServletRequestBuilder request = get("/firestation?stationNumber={stationNumber}", testStationNumber).accept(APPLICATION_JSON);
        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}
