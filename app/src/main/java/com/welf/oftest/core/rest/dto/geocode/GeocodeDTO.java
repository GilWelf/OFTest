package com.welf.oftest.core.rest.dto.geocode;

import com.google.gson.annotations.Expose;

/**
 * DTO representing the json response of the LocationIQ api call
 */
public class GeocodeDTO {

    @Expose
    private String lat;
    @Expose
    private String lon;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
