package com.bigdecimal.clasnapp.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundException extends EntityNotFoundException {
    public NotFoundException(String message) {
        super(message);
    }
}
