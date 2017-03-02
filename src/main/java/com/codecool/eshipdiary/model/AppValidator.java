package com.codecool.eshipdiary.model;

import lombok.Data;

@Data
public class AppValidator {
    private String apiKey;

    public AppValidator(String apiKey) {
        this.apiKey = apiKey;
    }
}
