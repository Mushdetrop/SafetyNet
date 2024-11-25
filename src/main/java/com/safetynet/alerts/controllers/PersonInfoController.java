package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.PersonInfoService;
import com.safetynet.alerts.views.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class PersonInfoController {

    private final PersonInfoService personInfoService;

    @Autowired
    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfo>> getPersonInfo(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        List<PersonInfo> personInfoList = personInfoService.getPersonInfo(firstName, lastName);

        if (personInfoList.isEmpty()) {
            // Return 404 if no matching persons are found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personInfoList);
    }
}

