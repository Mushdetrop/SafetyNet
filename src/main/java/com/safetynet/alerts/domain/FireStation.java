package com.safetynet.alerts.domain;

public class FireStation {
    private int stationNumber;
    private String address;

    public FireStation(String address, int stationNumber) {
        this.stationNumber = stationNumber;
        this.address = address;
    }
    //Getters and Setters
    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
