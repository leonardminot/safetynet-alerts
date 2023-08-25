package com.safetynet.safetynetalerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ApiResourceException extends RuntimeException{
    public ApiResourceException(String message) {
        super(message);
    }
}
