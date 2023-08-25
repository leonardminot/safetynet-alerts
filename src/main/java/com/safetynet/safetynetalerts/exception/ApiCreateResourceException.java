package com.safetynet.safetynetalerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ApiCreateResourceException extends RuntimeException{
    public ApiCreateResourceException(String message) {
        super(message);
    }
}
