package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.PhoneAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PhoneAlertServiceTest {

    @Mock
    private DataRepo dataRepo;

    @InjectMocks
    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPhoneNumbersByFirestation_WhenResidentsExist() {
        // Arrange
        int stationNumber = 3;

        List<FireStation> mockFireStations = Arrays.asList(
                new FireStation("1509 Culver St", 3),
                new FireStation("834 Binoc Ave", 3),
                new FireStation("748 Townings Dr", 3),
                new FireStation("112 Steppes Pl", 3)
        );

        List<Person> mockPeople = Arrays.asList(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "841-874-6512", "97451", "john.boyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "841-874-6513", "97451", "jacob.boyd@email.com"),
                new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", "841-874-6512", "97451", "tessa@email.com"),
                new Person("Clive", "Ferguson", "748 Townings Dr", "Culver", "841-874-6741", "97451", "clive@email.com"),
                new Person("Ron", "Peters", "112 Steppes Pl", "Culver", "841-874-8888", "97451", "ron@email.com")
        );

        when(dataRepo.getFireStations()).thenReturn(mockFireStations);
        when(dataRepo.getPeople()).thenReturn(mockPeople);

        // Act
        PhoneAlert result = phoneAlertService.getPhoneNumbersByFirestation(stationNumber);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getPhoneNumbers().size());
        assertTrue(result.getPhoneNumbers().contains("841-874-6512"));
        assertTrue(result.getPhoneNumbers().contains("841-874-6513"));
        assertTrue(result.getPhoneNumbers().contains("841-874-6512"));
        assertTrue(result.getPhoneNumbers().contains("841-874-6741"));
        assertTrue(result.getPhoneNumbers().contains("841-874-8888"));
    }

    @Test
    void testGetPhoneNumbersByFirestation_WhenNoResidentsExist() {
        // Arrange
        int stationNumber = 5;

        List<FireStation> mockFireStations = Arrays.asList(
                new FireStation("1509 Culver St", 3),
                new FireStation("748 Townings Dr", 3)
        );

        List<Person> mockPeople = Arrays.asList(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john.boyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "jacob.boyd@email.com")
        );

        when(dataRepo.getFireStations()).thenReturn(mockFireStations);
        when(dataRepo.getPeople()).thenReturn(mockPeople);

        // Act
        PhoneAlert result = phoneAlertService.getPhoneNumbersByFirestation(stationNumber);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPhoneNumbers().size());
    }

    @Test
    void testGetPhoneNumbersByFirestation_WhenNoMatchingFirestation() {
        // Arrange
        int stationNumber = 4;

        when(dataRepo.getFireStations()).thenReturn(new ArrayList<>());
        when(dataRepo.getPeople()).thenReturn(new ArrayList<>());

        // Act
        PhoneAlert result = phoneAlertService.getPhoneNumbersByFirestation(stationNumber);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPhoneNumbers().size());
    }
}

