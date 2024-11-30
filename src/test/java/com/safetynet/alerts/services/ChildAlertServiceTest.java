package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.ChildAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChildAlertServiceTest {

    @Mock
    private DataRepo dataRepo;

    @InjectMocks
    private ChildAlertService childAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChildrenAtAddress_WhenChildrenExist() {
        // Arrange
        String address = "1509 Culver St";

        List<Person> mockPeople = Arrays.asList(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john.boyd@email.com"),
                new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenley.boyd@email.com"),
                new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "roger.boyd@email.com")
        );

        List<MedicalRecord> mockMedicalRecords = Arrays.asList(
                new MedicalRecord("John", "Boyd", Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), Arrays.asList("nillacilan"), "03/06/1984"),
                new MedicalRecord("Tenley", "Boyd", new ArrayList<>(), Arrays.asList("peanut"), "02/18/2012"),
                new MedicalRecord("Roger", "Boyd", new ArrayList<>(), new ArrayList<>(), "09/06/2017")
        );

        when(dataRepo.getPeople()).thenReturn(mockPeople);
        when(dataRepo.getMedicalRecords()).thenReturn(mockMedicalRecords);

        // Act
        ChildAlert result = childAlertService.getChildrenAtAddress(address);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getChildren().size());
        assertEquals(1, result.getOtherResidents().size());

        // Verify children's details
        assertEquals("Tenley", result.getChildren().get(0).getFirstName());
        assertEquals("Roger", result.getChildren().get(1).getFirstName());
        assertEquals(12, result.getChildren().get(0).getAge()); // Assuming current year is 2024
        assertEquals(7, result.getChildren().get(1).getAge());

        // Verify other resident's details
        assertEquals("John", result.getOtherResidents().get(0).getFirstName());
    }

    @Test
    void testGetChildrenAtAddress_WhenNoChildrenExist() {
        // Arrange
        String address = "29 15th St";

        List<Person> mockPeople = Arrays.asList(
                new Person("Jonanathan", "Marrack", "29 15th St", "Culver", "97451", "841-874-6513", "jon.marrack@email.com")
        );

        List<MedicalRecord> mockMedicalRecords = Arrays.asList(
                new MedicalRecord("Jonanathan", "Marrack", new ArrayList<>(), new ArrayList<>(), "01/03/1989")
        );

        when(dataRepo.getPeople()).thenReturn(mockPeople);
        when(dataRepo.getMedicalRecords()).thenReturn(mockMedicalRecords);

        // Act
        ChildAlert result = childAlertService.getChildrenAtAddress(address);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetChildrenAtAddress_WhenAddressNotFound() {
        // Arrange
        String address = "Unknown Address";

        when(dataRepo.getPeople()).thenReturn(new ArrayList<>());

        // Act
        ChildAlert result = childAlertService.getChildrenAtAddress(address);

        // Assert
        assertNull(result);
    }
}
