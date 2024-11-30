package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.FloodAlertService;
import com.safetynet.alerts.views.FloodAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class FloodAlertController {

    private final FloodAlertService floodAlertService;

    @Autowired
    private FloodAlertController(FloodAlertService floodAlertService) {
        this.floodAlertService = floodAlertService;
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<FloodAlert> getFloodAlertByStations(@RequestParam("stations") String stationNumbers) {
        // Parse station numbers from the query parameter
        List<Integer> stationList = Arrays.stream(stationNumbers.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        FloodAlert floodAlert = floodAlertService.getFloodAlertByStations(stationList);

        if (floodAlert.getHouseholds().isEmpty()) {
            // Return 404 if no households are found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(floodAlert);
    }
}

