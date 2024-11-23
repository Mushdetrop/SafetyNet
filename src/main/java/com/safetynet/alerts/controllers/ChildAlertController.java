package com.safetynet.alerts.controllers;

import com.safetynet.alerts.services.ChildAlertService;
import com.safetynet.alerts.views.ChildAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ChildAlertController {

    @Autowired
    private ChildAlertService childAlertService;

    @GetMapping("/childAlert")
    public ChildAlert getChildrenAtAddress(@RequestParam("address") String address) {
        return childAlertService.getChildrenAtAddress(address);
    }
}
