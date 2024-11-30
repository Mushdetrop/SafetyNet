package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.FireAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FireAlertServiceTest {

    private FireAlertService fireAlertService;

    @Mock
    private DataRepo dataRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fireAlertService = new FireAlertService(dataRepo);
    }

    @Test
    void testGetFireAlertByAddress_ValidAddress() {
        // Mock FireStation data
        when(dataRepo.getFireStations()).thenReturn(
                List.of(new FireStation("1509 Culver St", 3))
        );

        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                List.of(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john@email.com"),
                        new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "tenz@email.com")
                )
        );

        // Mock MedicalRecord data
        when(dataRepo.getMedicalRecords()).thenReturn(
                List.of(
                        new MedicalRecord("John", "Boyd", Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"), "03/06/1984"),
                        new MedicalRecord("Tenley", "Boyd", Collections.emptyList(), Arrays.asList("peanut"), "02/18/2012")
                )
        );

        // Call the service method
        FireAlert fireAlert = fireAlertService.getFireAlertByAddress("1509 Culver St");

        // Validate FireAlert
        assertNotNull(fireAlert, "FireAlert should not be null for a valid address.");
        assertEquals(3, fireAlert.getFirestationNumber(), "Firestation number should be 3.");
        assertEquals(2, fireAlert.getResidents().size(), "Residents list should contain 2 entries.");

        // Validate first Resident
        FireAlert.Resident resident1 = fireAlert.getResidents().get(0);
        assertEquals("John", resident1.getFirstName(), "First resident's first name should match.");
        assertEquals(40, resident1.getAge(), "First resident's age should match.");
        assertTrue(resident1.getMedications().contains("aznol:350mg"), "Resident's medications should include 'aznol:350mg'.");
        assertTrue(resident1.getAllergies().contains("nillacilan"), "Resident's allergies should include 'nillacilan'.");

        // Validate second Resident
        FireAlert.Resident resident2 = fireAlert.getResidents().get(1);
        assertEquals("Tenley", resident2.getFirstName(), "Second resident's first name should match.");
        assertEquals(12, resident2.getAge(), "Second resident's age should match.");
        assertTrue(resident2.getAllergies().contains("peanut"), "Second resident's allergies should include 'peanut'.");
    }

    @Test
    void testGetFireAlertByAddress_NoFireStation() {
        // Mock FireStation data
        when(dataRepo.getFireStations()).thenReturn(
                List.of(new FireStation("123 Unknown St", 2))
        );

        // Mock Person and MedicalRecord data
        when(dataRepo.getPeople()).thenReturn(Collections.emptyList());
        when(dataRepo.getMedicalRecords()).thenReturn(Collections.emptyList());

        // Call the service method
        FireAlert fireAlert = fireAlertService.getFireAlertByAddress("1509 Culver St");

        // Validate FireAlert
        assertNull(fireAlert, "FireAlert should be null if no fire station covers the address.");
    }

    @Test
    void testGetFireAlertByAddress_NoResidents() {
        // Mock FireStation data
        when(dataRepo.getFireStations()).thenReturn(
                List.of(new FireStation("1509 Culver St", 3))
        );

        // Mock Person and MedicalRecord data
        when(dataRepo.getPeople()).thenReturn(Collections.emptyList());
        when(dataRepo.getMedicalRecords()).thenReturn(Collections.emptyList());

        // Call the service method
        FireAlert fireAlert = fireAlertService.getFireAlertByAddress("1509 Culver St");

        // Validate FireAlert
        assertNotNull(fireAlert, "FireAlert should not be null even if there are no residents.");
        assertEquals(3, fireAlert.getFirestationNumber(), "Firestation number should match.");
        assertTrue(fireAlert.getResidents().isEmpty(), "Residents list should be empty.");
    }
}
