package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.ChildAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildAlertService {

    private final DataRepo dataRepo;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public ChildAlertService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public ChildAlert getChildrenAtAddress(String address) {
        // Step 1: Filter residents by address
        List<Person> residents = dataRepo.getPeople().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        // Step 2: Separate children and other residents
        //TODO return an empty string if no children at the address.Add a condition for empty array
        List<ChildAlert.Child> children = residents.stream()
                .filter(this::isChild)
                .map(this::mapToChild)
                .collect(Collectors.toList());

        List<ChildAlert.Resident> otherResidents = residents.stream()
                .filter(person -> !isChild(person))
                .map(this::mapToResident)
                .collect(Collectors.toList());

        // Step 3: Return ChildAlertView object
        return new ChildAlert(children, otherResidents);
    }

    private boolean isChild(Person person) {
        return calculateAge(person) < 18;
    }

    private int calculateAge(Person person) {
        return dataRepo.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                        record.getLastName().equals(person.getLastName()))
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

    private ChildAlert.Child mapToChild(Person person) {
        int age = calculateAge(person);
        return new ChildAlert.Child(person.getFirstName(), person.getLastName(), age);
    }

    private ChildAlert.Resident mapToResident(Person person) {
        return new ChildAlert.Resident(
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                person.getPhone()
        );
    }
}