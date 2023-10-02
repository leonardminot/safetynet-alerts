package com.safetynet.safetynetalerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiRepositoryException extends RuntimeException {
    public ApiRepositoryException(String message) {
        super(message);
    }
}
