package com.safetynet.alerts.views;

import java.util.List;

public class FireStationPeople {
    private List<FireStationPerson> people;
    private Summary summary;

    //TODO
    //Create Parameterize constructor that take people, no of adults and no.of children.
    public FireStationPeople(List<FireStationPerson> people, int adults, int children) {
        this.people = people;
        this.summary = new Summary(adults, children);
    }


    public List<FireStationPerson> getPeople() {
        return people;
    }


    public Summary getSummary() {
        return summary;
    }


    public static class FireStationPerson {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;

        public FireStationPerson(String firstName, String lastName, String address, String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.phone = phone;
        }

        public String getFirstName() {
            return firstName;
        }


        public String getLastName() {
            return lastName;
        }


        public String getAddress() {
            return address;
        }


        public String getPhone() {
            return phone;
        }

    }

    public static class Summary {
        private int adults;
        private int children;

        public Summary(int adults, int children) {
            this.adults = adults;
            this.children = children;
        }

        public int getAdults() {
            return adults;
        }


        public int getChildren() {
            return children;
        }

    }
}
