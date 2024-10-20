package com.safetynet.alerts.repository;


import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.domain.Person;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.jsoniter.JsonIterator;

import java.util.List;

import com.jsoniter.any.Any;


@Repository
public class DataRepo {
    private static final String DATA_PATH = "src/main/resources/data.json";
    private final List<Person> people;
    private final List<FireStation> fireStations;
    private static Logger ILoggerFactory;
    private static final Logger logger = ILoggerFactory;
    private final List<MedicalRecord> medicalRecords;
    private Long id;
    private Person updatedPerson;

    public DataRepo(List<Person> people) {
        this.people = parsePeopleJson();
        this.fireStations = parseFireStationsJson();
        try {
            this.medicalRecords = parseMedicalRecordsJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TODO parse medical records
    }

    private List<Person> parsePeopleJson() {
        List<Person> people = new ArrayList<>();
        try {
            byte[] bytesFile = Files.readAllBytes(Paths.get(DATA_PATH));
            JsonIterator iter = JsonIterator.parse(bytesFile);
            Any any = iter.readAny();
            Any personAny = any.get("persons");
            personAny.forEach(a -> {
                String firstName = a.get("firstName").toString();
                String lastName = a.get("lastName").toString();
                String address = a.get("address").toString();
                String city = a.get("city").toString();
                String phone = a.get("phone").toString();
                String zip = a.get("zip").toString();
                String email = a.get("email").toString();
                people.add(new Person(firstName, lastName, address, city, phone, zip, email));
            });
        } catch (IOException e) {
            //TODO add logging & create Data Repo for FireStation & Medical Record. Data Repo Parse for order person
            logger.error("Error reading JSON file at path: " + DATA_PATH, e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return people;
    }

    public List<Person> getPeople() {
        return people;
    }

    @NotNull
    private List<FireStation> parseFireStationsJson() {
        List<FireStation> fireStations = new ArrayList<>();
        try {
            byte[] bytesFile = Files.readAllBytes(Paths.get(DATA_PATH));
            JsonIterator iter = JsonIterator.parse(bytesFile);
            Any any = iter.readAny();
            Any fireStationAny = any.get("firestations");

            fireStationAny.forEach(a -> {
                String address = a.get("address").toString();
                int stationNumber = a.get("station").toInt();
                fireStations.add(new FireStation(address, stationNumber));
            });

        } catch (IOException e) {
            logger.error("Error reading JSON file at path: " + DATA_PATH, e);
            throw new RuntimeException("Failed to parse fire station data from JSON", e);
        }
        return fireStations;
    }

    public List<FireStation> getFireStations() {
        return fireStations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    private List<MedicalRecord> parseMedicalRecordsJson() throws IOException {
        List<MedicalRecord> medicalrecords = new ArrayList<>();
        try {
            byte[] bytesFile = Files.readAllBytes(Paths.get(DATA_PATH));
            JsonIterator iter = JsonIterator.parse(bytesFile);
            Any any = iter.readAny();
            Any medicalRecordAny = any.get("medical records");

            medicalRecordAny.forEach(a -> {
                String medications = a.get("medications").toString();
                String allergies = a.get("allergies").toString();
                String birthdate = a.get("birthdate").toString();
                medicalrecords.add(new MedicalRecord(medications, allergies, birthdate));
            });
        } catch (IOException e) {
            logger.error("Error reading JSON file at path: " + DATA_PATH, e);
            throw new RuntimeException("Failed to parse fire station data from JSON", e);
        }
        return medicalrecords;

    }

    public void addPerson(Person person) {
        // Add the person to the list
        people.add(person);
    }

//    public Person getPersonById(String firstName) {
//        // Assuming Person class has a getId() method to retrieve its unique ID
//        return people.stream()
//                .filter(person -> person.getId().equals(this.id))
//                .findFirst()
//                .orElse(null); // Return null if not found
//    }

    public Person updatePerson(Person updatePerson) {
        Person existingPerson = getPersonByFullName(updatedPerson.getFirstName(), updatePerson.getLastName());

        if (existingPerson != null) {
            existingPerson.setFirstName(updatedPerson.getFirstName());
            existingPerson.setLastName(updatedPerson.getLastName());
            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
//            existingPerson.setMedicalRecord(updatedPerson.getMedicalRecord());
        }
        return existingPerson;
    }

    public Person getPersonByFullName(String firstName, String lastName) {
        return people.stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

}



