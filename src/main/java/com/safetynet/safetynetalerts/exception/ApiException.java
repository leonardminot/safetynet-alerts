package com.safetynet.safetynetalerts.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(
        String message,
        @JsonIgnore Throwable throwable,
        HttpStatus httpStatus,
        ZonedDateTime zonedDateTime) {}
