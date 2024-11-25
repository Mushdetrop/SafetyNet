package com.safetynet.alerts.views;

import java.util.List;

public class PhoneAlert {

    private int stationNumber;
    private List<String> phoneNumbers;

    public PhoneAlert(int stationNumber, List<String> phoneNumbers) {
        this.stationNumber = stationNumber;
        this.phoneNumbers = phoneNumbers;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}
