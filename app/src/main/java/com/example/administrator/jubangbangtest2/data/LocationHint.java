package com.example.administrator.jubangbangtest2.data;

import com.amap.api.services.core.LatLonPoint;

public class LocationHint {

    private String locationName;
    private String locationAddress;
    private LatLonPoint latLonPoint;

    public LocationHint(String locationName, String locationAddress,LatLonPoint latLonPoint) {
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.latLonPoint=latLonPoint;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        locationAddress = locationAddress;
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
    }
}
