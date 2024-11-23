package com.safetynet.alerts.controllers;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.services.ResponderService;
import com.safetynet.alerts.views.FireStationPeople;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        List<FireStationPeople.FireStationPerson> mockPeople = Arrays.asList(
                new FireStationPeople.FireStationPerson("John", "Boyd", "1509 Culver St", "841-874-6512"),
                new FireStationPeople.FireStationPerson("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784")
        );
        FireStationPeople mockResponse = new FireStationPeople(mockPeople, 1, 0);

        when(responderService.getPeopleByStation(testStationNumber)).thenReturn(mockResponse);
        mockMvc.perform(get("/api/firestation")
                        .param("stationNumber", String.valueOf(testStationNumber))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.people").isArray())
                .andExpect(jsonPath("$.people[0].firstName").value("John"))
                .andExpect(jsonPath("$.people[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.summary.adults").value(1))
                .andExpect(jsonPath("$.summary.children").value(0));
    }

    // mockMvc.perform(get("/api/firestation")
    //               .param("stationNumber", String.valueOf(testStationNumber))
    //             .accept(APPLICATION_JSON))
    //   .andExpect(status().isOk());
    //TODO imporve expectations of the test as no.of people, no of adults & no of children.
    @Test
    public void testGetPeopleByStation() {
        // Arrange: Mock data
        List<FireStationPeople.FireStationPerson> mockPeople = Arrays.asList(
                new FireStationPeople.FireStationPerson("John", "Boyd", "1509 Culver St", "841-874-6512"),
                new FireStationPeople.FireStationPerson("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784")
        );
        FireStationPeople mockResponse = new FireStationPeople(mockPeople, 1, 1);

        // Mock the service response
        when(responderService.getPeopleByStation(1)).thenReturn(mockResponse);

        // Act
        FireStationPeople result = responderService.getPeopleByStation(1);

        DataRepo mockRepo = Mockito.mock(DataRepo.class);

        assertEquals(2, result.getPeople().size());
        assertEquals(1, result.getSummary().getAdults());
        assertEquals(1, result.getSummary().getChildren());
    }

}
