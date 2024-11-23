
package com.safetynet.alerts.views;

import java.util.List;

/**
 * hold information for children & other residents (if present) served by a fireStation
 */
public class ChildAlert {

    private List<Child> children;
    private List<Resident> otherResidents;

    /**
     * Create child alerts information for given children and residents.
     *
     * @param children list of children at an address.
     * @param otherResidents List of residents living at the address.
     */
    public ChildAlert(List<Child> children, List<Resident> otherResidents) {
        this.children = children;
        this.otherResidents = otherResidents;
    }

    public List<Child> getChildren() {
        return children;
    }

    public List<Resident> getOtherResidents() {
        return otherResidents;
    }

    public static class Child {
        private String firstName;
        private String lastName;
        private int age;

        public Child(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAge() {
            return age;
        }
    }

    public static class Resident {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;

        public Resident(String firstName, String lastName, String address, String phone) {
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
}

