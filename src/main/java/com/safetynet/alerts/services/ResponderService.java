package com.safetynet.alerts.services;


import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.ChildAlert;
import com.safetynet.alerts.views.FireStationPeople;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponderService {
    private final DataRepo dataRepo;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public ResponderService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public FireStationPeople getPeopleByStation(int stationNumber) {
        //TODO
        //PSUEDO Code - Find all the people from Data Repo
        //Filter people by stationNumber
        //Count all the  People for Under 18, DOB & Current Date.
        //count all the adults in service area
        //create a fireStationPeople Object it must contain people & summary i.e FireStation People.
        List<Person> filteredPeople = filterPeopleByStation(stationNumber);

        // Step 2: Map to FireStationPerson and count adults/children
        List<FireStationPeople.FireStationPerson> fireStationPeople = mapToFireStationPeople(filteredPeople);

        int adults = 0;
        int children = 0;

        for (Person person : filteredPeople) {
            int age = calculateAge(person);
            if (age < 18) {
                children++;
            } else {
                adults++;
            }
        }

        // Step 3: Return FireStationPeople object
        return new FireStationPeople(fireStationPeople, adults, children);
    }

    private List<Person> filterPeopleByStation(int stationNumber) {
        return dataRepo.getPeople().stream()
                .filter(person -> dataRepo.getFireStations().stream()
                        .anyMatch(fs -> fs.getStationNumber() == stationNumber &&
                                fs.getAddress().equals(person.getAddress())))
                .collect(Collectors.toList());
    }

    private List<FireStationPeople.FireStationPerson> mapToFireStationPeople(List<Person> people) {
        return people.stream()
                .map(person -> new FireStationPeople.FireStationPerson(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()))
                .collect(Collectors.toList());
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

    @Service
    public class ChildAlertServices {

        private final DataRepo dataRepo;
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        @Autowired
        public ChildAlertServices(DataRepo dataRepo) {
            this.dataRepo = dataRepo;
        }

        public ChildAlert getChildrenAtAddress(String address) {
            // Step 1: Filter residents by address
            List<Person> residents = dataRepo.getPeople().stream()
                    .filter(person -> person.getAddress().equalsIgnoreCase(address))
                    .collect(Collectors.toList());

            // Step 2: Separate children and other residents
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

}
