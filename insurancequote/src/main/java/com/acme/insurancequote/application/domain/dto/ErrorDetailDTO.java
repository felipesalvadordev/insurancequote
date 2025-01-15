package com.acme.insurancequote.application.domain.dto;

public class ErrorDetailDTO {

    private String item;
    private String description;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
