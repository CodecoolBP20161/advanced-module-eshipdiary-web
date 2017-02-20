package com.codecool.eshipdiary.model;

import lombok.Data;

@Data
public class APIKeyValidator {
    private boolean valid;

    public APIKeyValidator(boolean isValid) {
        this.valid = isValid;
    }

}
