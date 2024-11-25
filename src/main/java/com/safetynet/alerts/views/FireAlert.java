package com.safetynet.alerts.views;

import java.util.List;

public class FireAlert {

    private int firestationNumber;
    private List<Resident> residents;

    public FireAlert(int firestationNumber, List<Resident> residents) {
        this.firestationNumber = firestationNumber;
        this.residents = residents;
    }

    public int getFirestationNumber() {
        return firestationNumber;
    }


    public List<Resident> getResidents() {
        return residents;
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
