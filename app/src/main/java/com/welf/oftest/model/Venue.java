package com.welf.oftest.model;

import java.io.Serializable;

public class Venue implements Serializable {

    private String name;
    private String formattedAddress;
    private String category;

    public Venue() {
    }

    public Venue(String name, String formattedAddress, String category) {
        this.name = name;
        this.formattedAddress = formattedAddress;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
