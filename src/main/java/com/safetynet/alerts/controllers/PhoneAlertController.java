package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.PhoneAlertService;
import com.safetynet.alerts.views.PhoneAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class PhoneAlertController {

    private final PhoneAlertService phoneAlertService;

    @Autowired
    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<PhoneAlert> getPhoneNumbersByFirestation(@RequestParam("firestation") int stationNumber) {
        PhoneAlert phoneAlert = phoneAlertService.getPhoneNumbersByFirestation(stationNumber);

        if (phoneAlert.getPhoneNumbers().isEmpty()) {
            // Return 404 if no phone numbers found
            return ResponseEntity.notFound().build();
        }

        // Return the PhoneAlert object
        return ResponseEntity.ok(phoneAlert);
    }
}
