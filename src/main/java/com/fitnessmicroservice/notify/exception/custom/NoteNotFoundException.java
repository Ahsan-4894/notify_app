package com.fitnessmicroservice.notify.exception.custom;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}

