package com.assignment.portal.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private String path;

    // Constructor
    public ErrorResponse(String message, HttpStatus status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }
}

