package com.safetynet.alerts.controllers;


import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PeopleController {
    Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    private DataRepo dataRepo;

    @PutMapping("/{firstName}/{lastName}")
    public Person updatePerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @RequestBody Person updatedPerson) {
        //public Person updatePerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        logger.info("updating person with name {}{}", firstName, lastName);
        Person person = dataRepo.getPersonByFullName(firstName, lastName);
        if (person != null) {
            // Apply updates
            person.setAddress(updatedPerson.getAddress());
            person.setCity(updatedPerson.getCity());
            person.setPhone(updatedPerson.getPhone());
            person.setZip(updatedPerson.getZip());
            person.setEmail(updatedPerson.getEmail());

            // Save the updated person back to the repository
            dataRepo.updatePerson(person);

            logger.info("Successfully updated person: {} {}", firstName, lastName);
            return person;
        } else {
            logger.error("Person not found: {} {}", firstName, lastName);
            return null; // Or return ResponseEntity with an appropriate status
        }
    }


    @GetMapping("")
    public List<Person> getAllPerson() {
        logger.info("getting all people");
        List<Person> people = dataRepo.getPeople();
        return people;


        //TODO get the people from the file
        //TODO ret type for this fn turn the people from the controller setting the returno be a List<Person>
//        MedicalRecord medicalRecord = new MedicalRecord();
//        Person person = new Person();
//        FireStation fireStation = new FireStation();
//        medicalRecord.setBirthdate("1/1/2021");
//        person.setFirstName("John");
//        person.setLastName("Boyd");
//        person.setAddress("1509 Culver St");
//        person.setMedicalRecord(medicalRecord);
//        person.setCity("Glendale");
//        person.setEmail("glen@glen.com");
//        person.setZip("60138");
//        person.setPhoneNumber("630363630");
//        return person;
    }

    //TODO add post mapping for creating the person
    //TODO add PUT mapping for updating an existing person
    //TODO delete mapping for removing a person
    @PostMapping("/add")
    public Person createPerson(@RequestBody Person person) {
        dataRepo.addPerson(person);
        return person;
    }


    //@PutMapping("/{firstName}/{lastName}")
    //public ResponseEntity<Person> updatePerson(@PathVariable("firstName") String firstName,
    //                                           @PathVariable("lastName") String lastName,
    //                                           @RequestBody Person updatedPerson) {
    //    logger.info("Updating person with name {} {}", firstName, lastName);
    //
    //    // Retrieve the person by full name
    //    Person person = dataRepo.getPersonByFullName(firstName, lastName);
    //    if (person == null) {
    //        logger.error("Person not found: {} {}", firstName, lastName);
    //        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //    }
    //
    //    // Update fields, ensuring that null values don't overwrite existing data
    //    if (updatedPerson.getFirstName() != null) person.setFirstName(updatedPerson.getFirstName());
    //    if (updatedPerson.getLastName() != null) person.setLastName(updatedPerson.getLastName());
    //    if (updatedPerson.getAddress() != null) person.setAddress(updatedPerson.getAddress());
    //    if (updatedPerson.getPhone() != null) person.setPhone(updatedPerson.getPhone());
    //    if (updatedPerson.getCity() != null) person.setCity(updatedPerson.getCity());
    //    if (updatedPerson.getZip() != null) person.setZip(updatedPerson.getZip());
    //    if (updatedPerson.getMedicalRecord() != null) person.setMedicalRecord(updatedPerson.getMedicalRecord());
    //
    //    // Update the person in the repository
    //    dataRepo.updatePerson(person);
    //
    //    // Log success and return the updated person
    //    logger.info("Successfully updated person: {} {}", firstName, lastName);
    //    return ResponseEntity.ok(person);
    //}

    @DeleteMapping("/{firstName}/{lastName}")
    public Object deletePerson(@PathVariable("firstName") String firstName,
                               @PathVariable("lastName") String lastName) {
        logger.info("Removing person with name {} {}", firstName, lastName);

        // Use the removePersonByFullName method with firstName and lastName
        boolean removed = dataRepo.removePersonByFullName(firstName, lastName);

        if (removed) {
            logger.info("Successfully deleted person: {} {}", firstName, lastName);
            return "Person with name " + firstName + " " + lastName + " deleted successfully.";
        } else {
            logger.error("Person not found: {} {}", firstName, lastName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
