package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.PersonInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonInfoServiceTest {

    private PersonInfoService personInfoService;

    @Mock
    private DataRepo dataRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personInfoService = new PersonInfoService(dataRepo);
    }

    @Test
    void testGetPersonInfo_ValidPerson() {
        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                Arrays.asList(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john@email.com"),
                        new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "tenz@email.com")
                )
        );

        // Mock MedicalRecord data
        when(dataRepo.getMedicalRecords()).thenReturn(
                Arrays.asList(
                        new MedicalRecord("John", "Boyd", Arrays.asList("aznol:350mg"), Arrays.asList("nillacilan"), "03/06/1984"),
                        new MedicalRecord("Tenley", "Boyd", Collections.emptyList(), Arrays.asList("peanut"), "02/18/2012")
                )
        );

        // Call the service method
        List<PersonInfo> personInfoList = personInfoService.getPersonInfo("John", "Boyd");

        // Validate PersonInfo
        assertNotNull(personInfoList, "PersonInfo list should not be null for a valid person.");
        assertEquals(1, personInfoList.size(), "PersonInfo list should contain one entry.");

        PersonInfo personInfo = personInfoList.get(0);
        assertEquals("John", personInfo.getFirstName(), "First name should match.");
        assertEquals("Boyd", personInfo.getLastName(), "Last name should match.");
        assertEquals(40, personInfo.getAge(), "Age should be calculated correctly.");
        assertEquals("1509 Culver St", personInfo.getAddress(), "Address should match.");
        assertTrue(personInfo.getMedications().contains("aznol:350mg"), "Medications should include 'aznol:350mg'.");
        assertTrue(personInfo.getAllergies().contains("nillacilan"), "Allergies should include 'nillacilan'.");
    }

    @Test
    void testGetPersonInfo_PersonNotFound() {
        // Mock empty Person data
        when(dataRepo.getPeople()).thenReturn(Collections.emptyList());
        when(dataRepo.getMedicalRecords()).thenReturn(Collections.emptyList());

        // Call the service method
        List<PersonInfo> personInfoList = personInfoService.getPersonInfo("Unknown", "Person");

        // Validate result
        assertNotNull(personInfoList, "PersonInfo list should not be null even if the person is not found.");
        assertTrue(personInfoList.isEmpty(), "PersonInfo list should be empty if the person is not found.");
    }

    @Test
    void testGetPersonInfo_NoMedicalRecord() {
        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                List.of(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john@email.com"))
        );

        // Mock empty MedicalRecord data
        when(dataRepo.getMedicalRecords()).thenReturn(Collections.emptyList());

        // Call the service method
        List<PersonInfo> personInfoList = personInfoService.getPersonInfo("John", "Boyd");

        // Validate PersonInfo
        assertNotNull(personInfoList, "PersonInfo list should not be null even if no medical record exists.");
        assertEquals(1, personInfoList.size(), "PersonInfo list should contain one entry.");

        PersonInfo personInfo = personInfoList.get(0);
        assertEquals("John", personInfo.getFirstName(), "First name should match.");
        assertEquals("Boyd", personInfo.getLastName(), "Last name should match.");
        assertEquals(0, personInfo.getAge(), "Age should be 0 if no medical record is found.");
        assertTrue(personInfo.getMedications().isEmpty(), "Medications should be empty.");
        assertTrue(personInfo.getAllergies().isEmpty(), "Allergies should be empty.");
    }
}
