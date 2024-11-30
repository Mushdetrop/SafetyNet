package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.ChildAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ResponderServiceTest {

    @Mock
    private DataRepo dataRepo;

    @InjectMocks
    private ResponderService responderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPeopleByStation() {
        // Mock data setup
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        List<Person> people = Arrays.asList(person1, person2);

        when(dataRepo.getPeople()).thenReturn(people);
        when(dataRepo.getFireStations()).thenReturn(Collections.singletonList(
                new FireStation("1509 Culver St", 3)
        ));

    }


    @Test
    public void testGetChildrenAtAddress() {
        // Mock data setup
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person person2 = new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        List<Person> people = Arrays.asList(person1, person2);

        when(dataRepo.getPeople()).thenReturn(people);
        when(dataRepo.getMedicalRecords()).thenReturn(Arrays.asList(
                new MedicalRecord("John", "Boyd", Collections.emptyList(), Collections.emptyList(), "03/06/1984"),
                new MedicalRecord("Roger", "Boyd", Collections.emptyList(), Collections.emptyList(), "09/06/2017")
        ));

        // Test
        ResponderService.ChildAlertServices childAlertServices = responderService.new ChildAlertServices(dataRepo);
        ChildAlert result = childAlertServices.getChildrenAtAddress("1509 Culver St");

        // Verify
        assertEquals(1, result.getChildren().size());
        assertEquals(1, result.getOtherResidents().size());
        assertEquals("Roger", result.getChildren().get(0).getFirstName());
    }

}
