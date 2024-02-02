package com.example.models.exceptions;

import com.example.models.enums.ErrorCode;

public class GenericBadRequestException extends GenericException {

    public GenericBadRequestException(ErrorCode code, String message) {
        super(code, message);
    }
}
