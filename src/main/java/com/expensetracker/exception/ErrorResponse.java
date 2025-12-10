package com.expensetracker.exception;

import lombok.Data;
import java.util.Map;

@Data
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(int status, String code, String message, Map<String, String> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}