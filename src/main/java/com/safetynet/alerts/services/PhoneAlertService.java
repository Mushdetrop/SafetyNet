package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.PhoneAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneAlertService {

    private final DataRepo dataRepo;

    @Autowired
    public PhoneAlertService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public PhoneAlert getPhoneNumbersByFirestation(int stationNumber) {
        // Step 1: Find all addresses covered by the fire station
        List<String> addresses = dataRepo.getFireStations().stream()
                .filter(fs -> fs.getStationNumber() == stationNumber)
                .map(fs -> fs.getAddress())
                .collect(Collectors.toList());

        // Step 2: Find all phone numbers of residents at those addresses
        List<String> phoneNumbers = dataRepo.getPeople().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct() // Avoid duplicate phone numbers
                .collect(Collectors.toList());

        // Step 3: Return a PhoneAlert object
        return new PhoneAlert(stationNumber, phoneNumbers);
    }
}
