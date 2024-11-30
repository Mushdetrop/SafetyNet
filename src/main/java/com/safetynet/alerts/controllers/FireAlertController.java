package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.FireAlertService;
import com.safetynet.alerts.views.FireAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class FireAlertController {

    private final FireAlertService fireAlertService;

    @Autowired
    private FireAlertController(FireAlertService fireAlertService) {
        this.fireAlertService = fireAlertService;
    }

    @GetMapping("/fire")
    public ResponseEntity<FireAlert> getFireAlertByAddress(@RequestParam("address") String address) {
        FireAlert fireAlert = fireAlertService.getFireAlertByAddress(address);

        if (fireAlert == null) {
            // Return 404 if no fire station or residents found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fireAlert);
    }
}
