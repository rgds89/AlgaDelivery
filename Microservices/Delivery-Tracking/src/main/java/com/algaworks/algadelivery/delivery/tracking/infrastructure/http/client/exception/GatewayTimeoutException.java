package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class GatewayTimeoutException extends RuntimeException {
    public GatewayTimeoutException() {
    }

    public GatewayTimeoutException(Throwable cause) {
        super(cause);
    }
    public GatewayTimeoutException(String message) {
        super(message);
    }
    public GatewayTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
