package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeopleController.class)
@Import(DataRepo.class)
public class PeopleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataRepo dataRepo;


    //public void testGetPeople() {
    //to create Junit for getPeople.
    @Test
    public void testGetAllPeople() throws Exception {
        // Arrange: Mock data
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "841-874-6512", "97451");
        // dataRepo.getPeople().addAll(Arrays.asList(person1));
        MockHttpServletRequestBuilder request = get("/api/people").accept(APPLICATION_JSON);
        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(23)));

        // .andExpect(jsonPath("$.length()").value(2))
        // .andExpect(jsonPath("$[0].firstName").value("John"))
        //.andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    //     Test for POST /api/people/add
//    @Test
//    public void testCreatePerson() throws Exception {
//        // Arrange
//        Person newPerson = new Person("Mark", "Smith", "789 Pine St", "New York", "1112223333", "mark.smith@example.com");
//
//        // Act & Assert
//        mockMvc.perform(post("/api/people/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newPerson)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("Mark"))
//                .andExpect(jsonPath("$.lastName").value("Smith"));
//    }
//
//    // Test for PUT /api/people/{firstName}/{lastName}
//    @Test
//    public void testUpdatePerson() throws Exception {
//        // Arrange: Add a person first to update
//        Person existingPerson = new Person("John", "Doe", "123 Main St", "Chicago", "1234567890", "john.doe@example.com");
//        dataRepo.getPeople().add(existingPerson);
//
//        Person updatedPerson = new Person("John", "Doe", "999 Updated St", "Chicago", "5555555555", "john.updated@example.com");
//
//        // Act & Assert
//        mockMvc.perform(put("/api/people/John/Doe")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedPerson)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.address").value("999 Updated St"))
//                .andExpect(jsonPath("$.phone").value("5555555555"))
//                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
//    }
    @Disabled
    @Test
    public void testUpdatePerson() throws Exception {
        // Arrange: Mock a person in the repository
        Person existingPerson = new Person("John", "Doe", "123 Main St", "Chicago", "1234567890", "60601");
        dataRepo.addPerson(existingPerson);

        // Updated person details
        Person updatedPerson = new Person("John", "Doe", "999 Updated St", "Chicago", "5555555555", "60699");

        // Act & Assert
        mockMvc.perform(put("/api/people/John/Doe")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("999 Updated St"))
                .andExpect(jsonPath("$.phone").value("5555555555"))
                .andExpect(jsonPath("$.zip").value("60699"));
    }

   @Disabled
   @Test
    public void testDeletePersonSuccess() throws Exception {
        // Arrange: Mock a person in the repository
        Person person = new Person("John", "Doe", "123 Main St", "Chicago", "1234567890", "60601");
        dataRepo.addPerson(person);

        // Act & Assert
        mockMvc.perform(delete("/api/people/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Person with name John Doe deleted successfully."));
    }


    @Test
    public void testDeletePersonNotFound() throws Exception {
        // Act & Assert: Attempt to delete a non-existing person
        mockMvc.perform(delete("/api/people/NonExistent/Person"))
                .andExpect(status().isNotFound());
    }


}
