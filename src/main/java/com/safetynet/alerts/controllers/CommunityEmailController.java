package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.CommunityEmailService;
import com.safetynet.alerts.views.CommunityEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    @Autowired
    private CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<CommunityEmail> getEmailsByCity(@RequestParam("city") String city) {
        CommunityEmail communityEmail = communityEmailService.getEmailsByCity(city);

        if (communityEmail.getEmailAddresses().isEmpty()) {
            // Return 404 if no emails are found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(communityEmail);
    }
}


