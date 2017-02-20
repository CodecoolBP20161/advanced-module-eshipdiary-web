package com.codecool.eshipdiary.model;

import lombok.Data;

@Data
public class AppValidator {
    private boolean valid;

    public AppValidator(boolean isValid) {
        this.valid = isValid;
    }

}
