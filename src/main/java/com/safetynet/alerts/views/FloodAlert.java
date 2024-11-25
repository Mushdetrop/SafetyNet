package com.safetynet.alerts.views;

import java.util.List;
import java.util.Map;

public class FloodAlert {

    private Map<String, List<Resident>> households;

    public FloodAlert(Map<String, List<Resident>> households) {
        this.households = households;
    }

    public Map<String, List<Resident>> getHouseholds() {
        return households;
    }

    public void setHouseholds(Map<String, List<Resident>> households) {
        this.households = households;
    }

    public static class Resident {
        private String firstName;
        private String lastName;
        private String phone;
        private int age;
        private List<String> medications;
        private List<String> allergies;

        public Resident(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.age = age;
            this.medications = medications;
            this.allergies = allergies;
        }

        public String getFirstName() {
            return firstName;
        }


        public String getLastName() {
            return lastName;
        }

        public String getPhone() {
            return phone;
        }

        public int getAge() {
            return age;
        }

        public List<String> getMedications() {
            return medications;
        }

        public List<String> getAllergies() {
            return allergies;
        }

    }
}
