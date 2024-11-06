package com.safetynet.alerts.repository;

import org.junit.jupiter.api.Test;
import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.domain.MedicalRecord;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataRepo.class})
class DataRepoTest {
    @Autowired
    private DataRepo dataRepo;

    @Test
    void getPeople() {
        List<Person> people = dataRepo.getPeople();
        int expectedSize = 23;

        // Check size of people list
        assertThat(people.size()).isEqualTo(expectedSize);

        // Check properties of a specific person (example: the first person in the list)
        Person firstPerson = people.get(0);
        assertThat(firstPerson.getFirstName()).isEqualTo("John");
        assertThat(firstPerson.getLastName()).isEqualTo("Boyd");
        assertThat(firstPerson.getAddress()).isEqualTo("1509 Culver St");
        assertThat(firstPerson.getCity()).isEqualTo("Culver");
        assertThat(firstPerson.getPhone()).isEqualTo("841-874-6512");
    }

    @Test
    void getFireStations() {
        List<FireStation> fireStations = dataRepo.getFireStations();
        int expectedSize = 13;

        // Check size of fire stations list
        assertThat(fireStations.size()).isEqualTo(expectedSize);

        // Check properties of a specific fire station (example: the first fire station)
        FireStation firstFireStation = fireStations.get(0);
        assertThat(firstFireStation.getAddress()).isEqualTo("1509 Culver St");
        assertThat(firstFireStation.getStationNumber()).isEqualTo(3);
    }

    @Test
    void getMedicalRecords() {
        List<MedicalRecord> medicalRecords = dataRepo.getMedicalRecords();
        int expectedSize = 23;

        // Check size of medical records list
        assertThat(medicalRecords.size()).isEqualTo(expectedSize);

        // Check properties of a specific medical record (example: the first medical record)
        MedicalRecord firstRecord = medicalRecords.get(0);
        assertThat(firstRecord.getFirstName()).isEqualTo("John");
        assertThat(firstRecord.getLastName()).isEqualTo("Boyd");
        assertThat(firstRecord.getBirthdate()).isEqualTo("03/06/1984");
        assertThat(firstRecord.getMedications()).contains("aznol:350mg", "hydrapermazol:100mg");
        assertThat(firstRecord.getAllergies()).contains("nillacilan");
    }

}
