package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonInfoService {

    private final DataRepo dataRepo;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public PersonInfoService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public List<PersonInfo> getPersonInfo(String firstName, String lastName) {
        // Step 1: Find all persons matching the first and last name
        List<Person> persons = dataRepo.getPeople().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName) &&
                        person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        // Step 2: Map persons to PersonInfo
        return persons.stream()
                .map(this::mapToPersonInfo)
                .collect(Collectors.toList());
    }

    private PersonInfo mapToPersonInfo(Person person) {
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

        return new PersonInfo(
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                age,
                person.getEmail(),
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
