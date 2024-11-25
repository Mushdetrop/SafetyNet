package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.FireAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireAlertService {

    private final DataRepo dataRepo;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public FireAlertService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public FireAlert getFireAlertByAddress(String address) {
        // Step 1: Find the fire station servicing the address
        int firestationNumber = dataRepo.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .map(fs -> fs.getStationNumber())
                .findFirst()
                .orElse(0);

        if (firestationNumber == 0) {
            // If no fire station is found, return null
            return null;
        }

        // Step 2: Get residents of the address
        List<FireAlert.Resident> residents = dataRepo.getPeople().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .map(this::mapToResident)
                .collect(Collectors.toList());

        // Step 3: Return FireAlert object
        return new FireAlert(firestationNumber, residents);
    }

    private FireAlert.Resident mapToResident(Person person) {
        int age = calculateAge(person);

        List<String> medications = dataRepo.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(person.getLastName()))
                .flatMap(record -> record.getMedications().stream())
                .collect(Collectors.toList());

        List<String> allergies = dataRepo.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(person.getLastName()))
                .flatMap(record -> record.getAllergies().stream())
                .collect(Collectors.toList());

        return new FireAlert.Resident(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                age,
                medications,
                allergies
        );
    }

    private int calculateAge(Person person) {
        return dataRepo.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(person.getLastName()))
                .map(record -> {
                    try {
                        return LocalDate.parse(record.getBirthdate(), dateFormatter);
                    } catch (Exception e) {
                        return null; // Handle invalid dates gracefully
                    }
                })
                .filter(dob -> dob != null)
                .map(dob -> Period.between(dob, LocalDate.now()).getYears())
                .findFirst()
                .orElse(0); // Default to 0 if no valid DOB is found
    }
}
