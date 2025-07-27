package com.algaworks.algadelivery.delivery.tracking.domain.exception;

public class DomainException extends RuntimeException{
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }
}
