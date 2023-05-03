package com.example.myapplication;

public class VenueModal {
    public String venueName;
    public String address;
    public String cityState;
    public String contactInfo;
    public String openHours;
    public String generalRule;
    public String childRule;


    public VenueModal(String venueName, String address, String cityState, String contactInfo, String openHours, String generalRule, String childRule) {
        this.venueName = venueName;
        this.address = address;
        this.cityState = cityState;
        this.contactInfo = contactInfo;
        this.openHours = openHours;
        this.generalRule = generalRule;
        this.childRule = childRule;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getAddress() {
        return address;
    }

    public String getCityState() {
        return cityState;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getOpenHours() {
        return openHours;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public String getChildRule() {
        return childRule;
    }
}
