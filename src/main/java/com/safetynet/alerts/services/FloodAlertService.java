package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.FloodAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FloodAlertService {

    private final DataRepo dataRepo;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public FloodAlertService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public FloodAlert getFloodAlertByStations(List<Integer> stationNumbers) {
        // Step 1: Find addresses served by the given fire stations
        List<String> addresses = dataRepo.getFireStations().stream()
                .filter(fs -> stationNumbers.contains(fs.getStationNumber()))
                .map(fs -> fs.getAddress())
                .collect(Collectors.toList());

        // Step 2: Group people by address and map to Resident
        Map<String, List<FloodAlert.Resident>> households = dataRepo.getPeople().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(
                        Person::getAddress,
                        Collectors.mapping(this::mapToResident, Collectors.toList())
                ));

        // Step 3: Return FloodAlert object
        return new FloodAlert(households);
    }

    private FloodAlert.Resident mapToResident(Person person) {
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

        return new FloodAlert.Resident(
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
