package com.safetynet.alerts.domain;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private String firstName;
    private String lastName;
    private List <String> medications;
    private List <String> allergies;
    private String birthdate;

    public MedicalRecord(String firstName, String lastName, List <String> medications, List<String> allergies, String birthdate) {
        this.medications = medications;
        this.allergies = allergies;
        this.birthdate = birthdate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List <String> getMedications() {
        return medications;
    }

    public void setMedications(List <String> medications) {
        this.medications = medications;
    }

    public List <String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List <String> allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", medications=" + medications +
                ", allergies=" + allergies +
                '}';
    }
}
