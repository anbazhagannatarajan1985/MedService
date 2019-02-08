package com.medicub.service.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidOrBadDataException extends Exception {
    public InvalidOrBadDataException(String message) {
        super(message);
    }
}
