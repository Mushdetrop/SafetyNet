package com.safetynet.alerts.controllers;


import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.repository.DataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/medicalrecords")
public class MedicalRecordController {
    @Autowired
    private DataRepo dataRepo;

    @GetMapping("")
    public List<MedicalRecord> getAllMedicalRecords() {
        // Fetch all medicalRecords from the repository
        List<MedicalRecord> medicalRecords = dataRepo.getMedicalRecords();
        return medicalRecords;
    }
}