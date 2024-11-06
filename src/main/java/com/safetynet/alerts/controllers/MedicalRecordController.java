package com.safetynet.alerts.controllers;


import com.safetynet.alerts.domain.MedicalRecord;
import com.safetynet.alerts.repository.DataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


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

    // Add a new medical record
    @PostMapping("/add")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord newMedicalRecord) {
        // Add the new medical record to the repository
        dataRepo.addMedicalRecord(newMedicalRecord);
        return newMedicalRecord;
    }

    // Update an existing medical record by ID
    @PutMapping("/{firstName}/{lastName}")
    public MedicalRecord updateMedicalRecord(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @RequestBody MedicalRecord updatedMedicalRecord) {
        // Update the medical record in the repository
        MedicalRecord existingMedicalRecord = dataRepo.updateMedicalRecord(updatedMedicalRecord);
        return existingMedicalRecord;
    }
}