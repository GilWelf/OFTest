package com.welf.oftest.core.rest.exception;

/**
 * Custom Exception to manage custom api call error
 */
public class CustomException extends Exception {

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }
}
