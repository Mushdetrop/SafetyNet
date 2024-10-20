package com.safetynet.alerts.controllers;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/firestations")
public class FireStationController {
    @Autowired
    private DataRepo dataRepo;

    @GetMapping("")
    public List<FireStation> getAllFireStations() {
        // Fetch all fire stations from the repository
        List<FireStation> fireStations = dataRepo.getFireStations();
        return fireStations;
    }
}