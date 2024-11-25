
package com.safetynet.alerts.views;

import java.util.List;

public class CommunityEmail {

    private String city;
    private List<String> emailAddresses;

    public CommunityEmail(String city, List<String> emailAddresses) {
        this.city = city;
        this.emailAddresses = emailAddresses;
    }

    public String getCity() {
        return city;
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

}
