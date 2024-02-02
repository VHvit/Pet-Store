package com.example.models.exceptions;

import com.example.models.enums.ErrorCode;

import java.util.UUID;

public class GenericNotFoundException extends GenericException {

    public GenericNotFoundException(ErrorCode code, UUID entityId) {
        super(code, "Entity with id " + entityId + " not found");
    }

}
