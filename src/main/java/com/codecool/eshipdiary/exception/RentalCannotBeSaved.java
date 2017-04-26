package com.codecool.eshipdiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Some items not available")
public class RentalCannotBeSaved extends RuntimeException {
    public RentalCannotBeSaved() {
        super();
    }
    public RentalCannotBeSaved(String message) {
        super(message);
    }
}
