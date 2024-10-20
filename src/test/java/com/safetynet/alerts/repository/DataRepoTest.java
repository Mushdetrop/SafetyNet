package com.safetynet.alerts.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataRepo.class})
class DataRepoTest {
    @Autowired
    private DataRepo dataRepo;

    @Test
    void getPeople() {
        int expected = 23;
        int actual = dataRepo.getPeople().size();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFireStations() {
        int expected = 13;
        int actual = dataRepo.getFireStations().size();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getMedicalRecord() {
        int expected = 23;
        int actual = dataRepo.getMedicalRecords().size();

        assertThat(actual).isEqualTo(expected);
    }


}
