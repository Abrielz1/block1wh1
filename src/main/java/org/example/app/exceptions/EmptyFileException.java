package org.example.app.exceptions;

public class EmptyFileException extends Exception {

    private final String message;

    public EmptyFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
