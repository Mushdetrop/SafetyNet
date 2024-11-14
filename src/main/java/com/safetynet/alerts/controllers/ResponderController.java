package com.safetynet.alerts.controllers;

//TODO
//Create a unit test
//Mapping station no to address in person
//create class for view models
//seperate package for view for eg FireStationPeople or Serviced People. Clarity is very important
//and related to the business.
//{"people": [...], "summary": {"adults": 34, "children": 23}}

import com.safetynet.alerts.views.FireStationPeople;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ResponderController {
   // @Autowired
    //private ResponderService responderService;
    @GetMapping ("/firestation")
    public FireStationPeople getFireStationPeople(@RequestParam ("stationNumber") int StationNumber){
        return new FireStationPeople();
    }
}

