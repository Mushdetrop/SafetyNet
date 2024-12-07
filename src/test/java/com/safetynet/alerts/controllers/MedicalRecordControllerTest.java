package com.safetynet.alerts.controllers;

import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.repository.DataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
@Import(DataRepo.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataRepo dataRepo;

    @BeforeEach
    public void setUp() {
        dataRepo.getMedicalRecords().clear();
        dataRepo.getMedicalRecords().addAll(Arrays.asList(
                new MedicalRecord(
                        "John",
                        "Boyd",
                        Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                        List.of("nillacilan"),
                        "03/06/1984"
                ),
                new MedicalRecord(
                        "Jacob",
                        "Boyd",
                        Arrays.asList("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"),
                        new ArrayList<>(),
                        "03/06/1989"
                ),
                new MedicalRecord(
                        "Tenley",
                        "Boyd",
                        new ArrayList<>(),
                        List.of("peanut"),
                        "02/18/2012"
                )
        ));
    }

    @Test
    public void testGetAllMedicalRecords() throws Exception {
        mockMvc.perform(get("/api/medicalrecords").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testAddMedicalRecord() throws Exception {
        String newMedicalRecordJson = """
                    {
                        "firstName": "Tessa",
                        "lastName": "Carman",
                        "birthdate": "02/18/2012",
                        "medications": [],
                        "allergies": []
                    }
                """;

        mockMvc.perform(post("/api/medicalrecords/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMedicalRecordJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Tessa"))
                .andExpect(jsonPath("$.lastName").value("Carman"))
                .andExpect(jsonPath("$.birthdate").value("02/18/2012"));
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        String updatedMedicalRecordJson = """
                    {
                        "firstName": "John",
                        "lastName": "Boyd",
                        "birthdate": "03/06/1984",
                        "medications": ["aznol:500mg"],
                        "allergies": ["peanut"]
                    }
                """;

        mockMvc.perform(put("/api/medicalrecords/John/Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMedicalRecordJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medications", hasSize(1)))
                .andExpect(jsonPath("$.medications[0]").value("aznol:500mg"))
                .andExpect(jsonPath("$.allergies", hasSize(1)))
                .andExpect(jsonPath("$.allergies[0]").value("peanut"));
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/api/medicalrecords/John/Boyd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Medical record for John Boyd deleted successfully."));
    }
}
