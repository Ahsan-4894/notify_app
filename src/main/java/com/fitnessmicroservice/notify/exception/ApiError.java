package com.fitnessmicroservice.notify.exception;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private HttpStatusCode statusCode;
    private String error;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }
    public ApiError(String error, HttpStatusCode status) {
        this();
        this.statusCode = status;
        this.error = error;
    }
}
