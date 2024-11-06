package com.safetynet.alerts.controllers;

import com.safetynet.alerts.domain.FireStation;
import com.safetynet.alerts.repository.DataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("")
    public FireStation addFireStation(@RequestBody FireStation newFireStation) {
        dataRepo.getFireStations().add(newFireStation);
        return newFireStation;
    }

    // Update an existing fire station by address
    @PutMapping("/{address}")
    public FireStation updateFireStation(@PathVariable String address, @RequestBody FireStation updatedFireStation) {
        return dataRepo.updateFireStation(address, updatedFireStation);
    }

    // Delete a fire station by address
    @DeleteMapping("/{address}")
    public String deleteFireStation(@PathVariable String address) {
        List<FireStation> fireStations = dataRepo.getFireStations();
        boolean removed = fireStations.removeIf(fireStation -> fireStation.getAddress().equalsIgnoreCase(address));

        if (removed) {
            return "Fire station at address " + address + " deleted successfully.";
        } else {
            return "Fire station at address " + address + " not found.";
        }
    }

}