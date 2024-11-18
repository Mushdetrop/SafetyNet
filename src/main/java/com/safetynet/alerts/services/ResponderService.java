package com.safetynet.alerts.services;


import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.FireStationPeople;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class ResponderService {
    private final DataRepo dataRepo;

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
        List<Person> allPeople = dataRepo.getPeople();
        List<Person> filteredPeople = allPeople.stream()
                .filter(person -> dataRepo.getFireStations().stream()
                        .anyMatch(fs -> fs.getStationNumber() == stationNumber && fs.getAddress().equals(person.getAddress())))
                .collect(Collectors.toList());
        List<FireStationPeople.FireStationPerson> fireStationPeople = filteredPeople.stream()
                .map(person -> new FireStationPeople.FireStationPerson(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()))
                .collect(Collectors.toList());

        int adults = 0;
        int children = 0;
        for (Person person : filteredPeople) {
            LocalDate dob = dataRepo.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName())
                            && record.getLastName().equals(person.getLastName()))
                    .map(record -> LocalDate.parse(record.getBirthdate()))
                    .findFirst()
                    .orElse(null);

            if (dob != null) {
                int age = Period.between(dob, LocalDate.now()).getYears();
                if (age < 18) {
                    children++;
                } else {
                    adults++;
                }
            }
        }

        return new FireStationPeople(fireStationPeople, adults, children);
    }
}
