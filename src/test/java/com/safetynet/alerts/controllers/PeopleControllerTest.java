package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeopleController.class)
public class PeopleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataRepo dataRepo;

    private List<Person> people;

    @BeforeEach
    public void setUp() {
        people = new ArrayList<>();
        people.add(new Person("John", "Boyd", "1509 Culver St", "Culver", "841-874-6512", "97451", "jaboyd@email.com"));
        people.add(new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "841-874-6513", "97451", "drk@email.com"));

        when(dataRepo.getPeople()).thenReturn(people);
        when(dataRepo.getPersonByFullName("John", "Boyd"))
                .thenReturn(people.stream()
                        .filter(p -> p.getFirstName().equals("John") && p.getLastName().equals("Boyd"))
                        .findFirst().orElse(null));
        when(dataRepo.removePersonByFullName("John", "Doe")).thenReturn(true);
        when(dataRepo.removePersonByFullName("NonExistent", "Person")).thenReturn(false);
    }

    @Test
    public void testGetAllPeople() throws Exception {
        mockMvc.perform(get("/api/people").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jacob"));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        // Arrange: Updated person data
        Person updatedPerson = new Person("John", "Boyd", "999 Updated St", "Chicago", "555-555-5555", "60699", "updated@email.com");

        // Mock update behavior
        when(dataRepo.updatePerson(any(Person.class))).thenAnswer(invocation -> {
            Person personToUpdate = invocation.getArgument(0);
            people.stream()
                    .filter(p -> p.getFirstName().equals(personToUpdate.getFirstName()) && p.getLastName().equals(personToUpdate.getLastName()))
                    .forEach(p -> {
                        p.setAddress(personToUpdate.getAddress());
                        p.setCity(personToUpdate.getCity());
                        p.setPhone(personToUpdate.getPhone());
                        p.setZip(personToUpdate.getZip());
                        p.setEmail(personToUpdate.getEmail());
                    });
            return personToUpdate;
        });

        mockMvc.perform(put("/api/people/John/Boyd")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("999 Updated St"))
                .andExpect(jsonPath("$.city").value("Chicago"))
                .andExpect(jsonPath("$.phone").value("555-555-5555"))
                .andExpect(jsonPath("$.zip").value("60699"))
                .andExpect(jsonPath("$.email").value("updated@email.com"));
    }
    @Test
    public void testDeletePersonSuccess() throws Exception {
        mockMvc.perform(delete("/api/people/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Person with name John Doe deleted successfully."));
    }

    @Test
    public void testDeletePersonNotFound() throws Exception {
        mockMvc.perform(delete("/api/people/NonExistent/Person"))
                .andExpect(status().isNotFound());
    }
}

