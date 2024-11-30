
package com.safetynet.alerts.services;

import com.safetynet.alerts.domain.Person;
import com.safetynet.alerts.repository.DataRepo;
import com.safetynet.alerts.views.CommunityEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityEmailServiceTest {

    private CommunityEmailService communityEmailService;

    @Mock
    private DataRepo dataRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        communityEmailService = new CommunityEmailService(dataRepo);
    }

    @Test
    void testGetEmailsByCity_ValidCity() {
        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                Arrays.asList(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                        new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"),
                        new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com"),
                        new Person("Bob", "Smith", "123 Main St", "OtherCity", "97452", "841-874-6533", "bob@email.com")
                )
        );

        // Call the service method
        CommunityEmail communityEmail = communityEmailService.getEmailsByCity("Culver");

        // Validate CommunityEmail
        assertNotNull(communityEmail, "CommunityEmail should not be null for a valid city.");
        assertEquals("Culver", communityEmail.getCity(), "City should match the input.");
        assertNotNull(communityEmail.getEmailAddresses(), "Emails list should not be null.");
        assertEquals(3, communityEmail.getEmailAddresses().size(), "There should be 3 unique emails for Culver.");
        assertTrue(communityEmail.getEmailAddresses().contains("jaboyd@email.com"), "Emails should include 'jaboyd@email.com'.");
        assertTrue(communityEmail.getEmailAddresses().contains("drk@email.com"), "Emails should include 'drk@email.com'.");
        assertTrue(communityEmail.getEmailAddresses().contains("tenz@email.com"), "Emails should include 'tenz@email.com'.");
    }

    @Test
    void testGetEmailsByCity_NoMatchingCity() {
        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                Arrays.asList(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                        new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com")
                )
        );

        // Call the service method
        CommunityEmail communityEmail = communityEmailService.getEmailsByCity("NonExistentCity");

        // Validate CommunityEmail
        assertNotNull(communityEmail, "CommunityEmail should not be null even if no matching city is found.");
        assertEquals("NonExistentCity", communityEmail.getCity(), "City should match the input.");
        assertNotNull(communityEmail.getEmailAddresses(), "Emails list should not be null.");
        assertTrue(communityEmail.getEmailAddresses().isEmpty(), "Emails list should be empty for a non-existent city.");
    }

    @Test
    void testGetEmailsByCity_DuplicateEmails() {
        // Mock Person data
        when(dataRepo.getPeople()).thenReturn(
                Arrays.asList(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                        new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com")
                )
        );

        // Call the service method
        CommunityEmail communityEmail = communityEmailService.getEmailsByCity("Culver");

        // Validate CommunityEmail
        assertNotNull(communityEmail, "CommunityEmail should not be null for a valid city.");
        assertEquals("Culver", communityEmail.getCity(), "City should match the input.");
        assertEquals(1, communityEmail.getEmailAddresses().size(), "Duplicate emails should be removed.");
        assertTrue(communityEmail.getEmailAddresses().contains("jaboyd@email.com"), "Emails should include 'jaboyd@email.com'.");
    }
}
