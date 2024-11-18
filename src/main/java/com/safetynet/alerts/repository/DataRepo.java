package com.safetynet.alerts.repository;


import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Repository
public class DataRepo {

    private static final Logger logger = LoggerFactory.getLogger(DataRepo.class);
    private static final String DATA_PATH = "src/main/resources/data.json";
    private final List<Person> people;
    private final List<FireStation> fireStations;
    private static Logger ILoggerFactory;
    private final List<MedicalRecord> medicalRecords;
    private Long id;
    private Person updatedPerson;

    public DataRepo() {
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
        List<MedicalRecord> records = new ArrayList<>();
        //return Array<medicalrecords> (new MedicalRecord())
        try {
            byte[] bytesFile = Files.readAllBytes(Paths.get(DATA_PATH));
            JsonIterator iter = JsonIterator.parse(bytesFile);
            Any any = iter.readAny();
            Any medicalRecordAny = any.get("medicalrecords");

            medicalRecordAny.forEach(record -> {
                String firstName = record.get("firstName").toString();
                String lastname = record.get("lastName").toString();
                String birthdate = record.get("birthdate").toString();

                List<String> medications = new ArrayList<>();
                record.get("medications").forEach(med -> medications.add(med.toString()));

                List<String> allergies = new ArrayList<>();
                record.get("allergies").forEach(allergy -> allergies.add(allergy.toString()));
                records.add(new MedicalRecord(firstName, lastname, medications, allergies, birthdate));
            });
        } catch (IOException e) {
            logger.error("Error reading JSON file at path: " + DATA_PATH, e);
            throw new RuntimeException("Failed to parse medical records data from JSON", e);
        }
        return records;

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

    public Person updatePerson(@NotNull Person updatePerson) {
        Person existingPerson = getPersonByFullName(updatePerson.getFirstName(), updatePerson.getLastName());

        if (existingPerson != null) {
            existingPerson.setFirstName(updatePerson.getFirstName());
            existingPerson.setLastName(updatePerson.getLastName());
            existingPerson.setAddress(updatePerson.getAddress());
            existingPerson.setPhone(updatePerson.getPhone());
            existingPerson.setCity(updatePerson.getCity());
            existingPerson.setZip(updatePerson.getZip());
            //      existingPerson.setMedicalRecord(updatedPerson.getMedicalRecord());
        }
        return existingPerson;
    }

    public Person getPersonByFullName(String firstName, String lastName) {
        return people.stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    public void addMedicalRecord(MedicalRecord newMedicalRecord) {
        // Add the new medical record to the list
        medicalRecords.add(newMedicalRecord);
        // Log action
        logger.info("New medical record added for: " + newMedicalRecord.getFirstName() + " " + newMedicalRecord.getLastName());
    }

    // Update an existing medical record by firstName and lastName
    public MedicalRecord updateMedicalRecord(MedicalRecord updatedMedicalRecord) {
        Optional<MedicalRecord> existingRecordOpt = medicalRecords.stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName())
                        && record.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName()))
                .findFirst();

        if (existingRecordOpt.isPresent()) {
            MedicalRecord existingRecord = existingRecordOpt.get();
            existingRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
            existingRecord.setMedications(updatedMedicalRecord.getMedications());
            existingRecord.setAllergies(updatedMedicalRecord.getAllergies());

            logger.info("Medical record updated for: {} {} " + existingRecord.getFirstName() + " " + existingRecord.getLastName());
            //logger.info(String.format("Medical record updated for: %s %s", existingRecord.getFirstName(), existingRecord.getLastName()));
            return existingRecord;
        } else {
            logger.warn("Medical record not found for: " + updatedMedicalRecord.getFirstName() + " " + updatedMedicalRecord.getLastName());
            return null; // or throw an exception, depending on the use case
        }
    }

    public FireStation updateFireStation(String address, FireStation updatedFireStation) {
        Optional<FireStation> existingFireStationOpt = fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .findFirst();

        if (existingFireStationOpt.isPresent()) {
            FireStation existingFireStation = existingFireStationOpt.get();
            existingFireStation.setStationNumber(updatedFireStation.getStationNumber());
            existingFireStation.setAddress(updatedFireStation.getAddress());

            logger.info("Fire station updated for address: " + existingFireStation.getAddress());
            return existingFireStation;
        } else {
            logger.warn("Fire station not found for address: " + address);
            return null; // or throw an exception, depending on the use case
        }
    }

    public boolean removePersonByFullName(String firstName, String lastName) {
        Optional<Person> personToRemove = Optional.ofNullable(getPersonByFullName(firstName, lastName));
        if (personToRemove.isPresent()) {
            people.remove(personToRemove.get());
            return true;
        }
        return false;
    }

    public boolean removeMedicalRecordByFullName(String firstName, String lastName) {
        Iterator<MedicalRecord> iterator = medicalRecords.iterator();
        while (iterator.hasNext()) {
            MedicalRecord record = iterator.next();
            if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)) {
                iterator.remove();
                return true; // Successfully removed
            }
        }
        return false; // Record not found
    }

}




