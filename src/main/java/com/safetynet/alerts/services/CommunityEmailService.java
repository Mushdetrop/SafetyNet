package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.CommunityEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityEmailService {

    private final DataRepo dataRepo;

    @Autowired
    public CommunityEmailService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public CommunityEmail getEmailsByCity(String city) {
        // Step 1: Find all emails for people living in the specified city
        List<String> emailAddresses = dataRepo.getPeople().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct() // Remove duplicate emails
                .collect(Collectors.toList());

        // Step 2: Return CommunityEmail object
        return new CommunityEmail(city, emailAddresses);
    }
}

