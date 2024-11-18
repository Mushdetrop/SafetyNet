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

    public void setPeople(List<FireStationPerson> people) {
        this.people = people;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
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

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public void setAdults(int adults) {
            this.adults = adults;
        }

        public int getChildren() {
            return children;
        }

        public void setChildren(int children) {
            this.children = children;
        }
    }
}
