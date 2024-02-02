package com.example.models.exceptions;

import com.example.models.enums.ErrorCode;
import lombok.Data;

@Data
public class GenericException extends RuntimeException {

    private final ErrorCode code;

    public GenericException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
