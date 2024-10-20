package com.safetynet.alerts.domain;

public class Person {
    //private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
   // private MedicalRecord medicalRecord;
   // private FireStation fireStation;
    private String phone;
    private String zip;
    private String email;

    public Person(String firstName, String lastName, String address, String city, String phone, String zip, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.zip = zip;
        this.email = email;
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

//   public MedicalRecord getMedicalRecord() {
//        return medicalRecord;
//    }

//    public void setMedicalRecord(MedicalRecord medicalRecord) {
//        this.medicalRecord = medicalRecord;
//    }

//    public FireStation getFireStation() {
//        return fireStation;
//    }

//    public void setFireStation (FireStation fireStation) {
//        this.fireStation = fireStation;
//    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Long getId(){
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }

}
