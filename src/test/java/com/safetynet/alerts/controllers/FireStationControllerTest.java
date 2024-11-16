package com.safetynet.alerts.controllers;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.repository.DataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireStationController.class)
@Import(DataRepo.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataRepo dataRepo;

    @BeforeEach
    public void setUp() {
        dataRepo.getFireStations().clear();
        dataRepo.getFireStations().addAll(Arrays.asList(
                new FireStation("1509 Culver St", 3),
                new FireStation("29 15th St", 2),
                new FireStation("834 Binoc Ave", 3)
        ));
    }

    @Test
    public void testGetAllFireStations() throws Exception {
        mockMvc.perform(get("/api/firestations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))) // Verifying size of the response array
                .andExpect(jsonPath("$[0].address", is("1509 Culver St")))
                .andExpect(jsonPath("$[0].stationNumber", is(3)));
    }

    @Test
    public void testAddFireStation() throws Exception {
        String newFireStationJson = """
            {
                "address": "112 Steppes Pl",
                "stationNumber": 4
            }
        """;

        mockMvc.perform(post("/api/firestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFireStationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("112 Steppes Pl")))
                .andExpect(jsonPath("$.stationNumber", is(4)));
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        String updatedFireStationJson = """
            {
                "address": "1509 Culver St",
                "stationNumber": 5
            }
        """;

        mockMvc.perform(put("/api/firestations/1509 Culver St")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFireStationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("1509 Culver St")))
                .andExpect(jsonPath("$.stationNumber", is(5)));
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        mockMvc.perform(delete("/api/firestations/1509 Culver St")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Fire station at address 1509 Culver St deleted successfully."));
    }

    @Test
    public void testDeleteFireStation_NotFound() throws Exception {
        mockMvc.perform(delete("/api/firestations/Unknown Address")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Fire station at address Unknown Address not found."));
    }
}
