package org.example.app.exceptions;

public class RegexException extends Exception {

    private final String message;

    public RegexException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
