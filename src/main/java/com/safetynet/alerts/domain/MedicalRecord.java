package com.safetynet.alerts.domain;

public class MedicalRecord {
    private String medications;
    private String allergies;
    private String birthdate;

    public MedicalRecord (String medications, String allergies, String birthdate){
        this.medications = medications;
        this.allergies = allergies;
        this.birthdate = birthdate;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getMedications(){
        return medications;
    }
    public void setMedications(String medications) {
        this.medications = medications;
    }
    public String getAllergies(){
        return allergies;
    }
    public void setAllergies(String allergies){
        this.allergies = allergies;
    }


}
