package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.FloodAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FloodAlertServiceTest {

    private FloodAlertService floodAlertService;

    @Mock
    private DataRepo dataRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        floodAlertService = new FloodAlertService(dataRepo);
    }

    @Test
    void testGetFloodAlert_ValidStations() {
        // Mock FireStation data
        when(dataRepo.getFireStations()).thenReturn(
                Arrays.asList(
                        new FireStation("1509 Culver St", 3),
                        new FireStation("29 15th St", 2)
                )
        );

        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                Arrays.asList(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john@email.com"),
                        new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "tenz@email.com"),
                        new Person("Jane", "Doe", "29 15th St", "Culver", "97451", "841-874-6543", "jane@email.com")
                )
        );

        // Mock MedicalRecord data
        when(dataRepo.getMedicalRecords()).thenReturn(
                Arrays.asList(
                        new MedicalRecord("John", "Boyd", Arrays.asList("aznol:350mg"), Arrays.asList("nillacilan"), "03/06/1984"),
                        new MedicalRecord("Tenley", "Boyd", Collections.emptyList(), Collections.emptyList(), "02/18/2012"),
                        new MedicalRecord("Jane", "Doe", Collections.emptyList(), Collections.emptyList(), "03/06/1990")
                )
        );

        // Call the service method
        List<Integer> stationNumbers = Arrays.asList(3, 2);
        FloodAlert floodAlert = floodAlertService.getFloodAlertByStations(stationNumbers);

        // Validate FloodAlert
        assertNotNull(floodAlert, "FloodAlert should not be null.");
        assertNotNull(floodAlert.getHouseholds(), "Households should not be null.");
        assertEquals(2, floodAlert.getHouseholds().size(), "There should be two households in the result.");

        // Validate residents at "1509 Culver St"
        List<FloodAlert.Resident> residents1509 = floodAlert.getHouseholds().get("1509 Culver St");
        assertNotNull(residents1509, "Residents at '1509 Culver St' should not be null.");
        assertEquals(2, residents1509.size(), "There should be 2 residents at '1509 Culver St'.");

        FloodAlert.Resident john = residents1509.get(0);
        assertEquals("John", john.getFirstName(), "First resident's first name should be 'John'.");
        assertEquals(40, john.getAge(), "John's age should be 40.");
        assertTrue(john.getMedications().contains("aznol:350mg"), "John's medications should include 'aznol:350mg'.");

        FloodAlert.Resident tenley = residents1509.get(1);
        assertEquals("Tenley", tenley.getFirstName(), "Second resident's first name should be 'Tenley'.");
        assertEquals(12, tenley.getAge(), "Tenley's age should be 12.");

        // Validate residents at "29 15th St"
        List<FloodAlert.Resident> residents2915 = floodAlert.getHouseholds().get("29 15th St");
        assertNotNull(residents2915, "Residents at '29 15th St' should not be null.");
        assertEquals(1, residents2915.size(), "There should be 1 resident at '29 15th St'.");

        FloodAlert.Resident jane = residents2915.get(0);
        assertEquals("Jane", jane.getFirstName(), "Resident's first name should be 'Jane'.");
        assertEquals(34, jane.getAge(), "Jane's age should be 34.");
    }

    @Test
    void testGetFloodAlert_NoStations() {
        // Mock FireStation data with no matching stations
        when(dataRepo.getFireStations()).thenReturn(Collections.emptyList());

        // Call the service method
        List<Integer> stationNumbers = Arrays.asList(99);
        FloodAlert floodAlert = floodAlertService.getFloodAlertByStations(stationNumbers);

        // Validate result
        assertNotNull(floodAlert, "FloodAlert should not be null even if no matching stations are found.");
        assertTrue(floodAlert.getHouseholds().isEmpty(), "Households should be empty when no matching stations are found.");
    }

//    @Test
//    void testGetFloodAlert_NoResidents() {
//        // Mock FireStation data
//        when(dataRepo.getFireStations()).thenReturn(
//                List.of(new FireStation("1509 Culver St", 3))
//        );
//
//        // Mock Person and MedicalRecord data with no matching residents
//        when(dataRepo.getPeople()).thenReturn(Collections.emptyList());
//        when(dataRepo.getMedicalRecords()).thenReturn(Collections.emptyList());
//
//        // Call the service method
//        List<Integer> stationNumbers = Arrays.asList(3);
//        FloodAlert floodAlert = floodAlertService.getFloodAlertByStations(stationNumbers);
//
//        // Validate result
//        assertNotNull(floodAlert, "FloodAlert should not be null.");
//        assertNotNull(floodAlert.getHouseholds(), "Households should not be null.");
//        assertTrue(floodAlert.getHouseholds().containsKey("1509 Culver St"), "Household for '1509 Culver St' should exist.");
//        assertTrue(floodAlert.getHouseholds().get("1509 Culver St").isEmpty(), "Residents list for '1509 Culver St' should be empty.");
//    }
}
