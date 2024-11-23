package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.ResponderService;
import com.safetynet.alerts.views.FireStationPeople;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ResponderController {
    @Autowired
    private ResponderService responderService;


    @GetMapping("/firestation")
    public FireStationPeople getFireStationPeople(@RequestParam("stationNumber") int stationNumber) {
        return responderService.getPeopleByStation(stationNumber);
    }


}

