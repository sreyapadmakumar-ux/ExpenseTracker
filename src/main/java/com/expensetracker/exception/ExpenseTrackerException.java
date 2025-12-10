package com.expensetracker.exception;

public class ExpenseTrackerException extends RuntimeException {
    private final String errorCode;

    public ExpenseTrackerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ExpenseTrackerException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static class EntityNotFoundException extends ExpenseTrackerException {
        public EntityNotFoundException(String entity, Object id) {
            super(String.format("%s not found with id: %s", entity, id), "NOT_FOUND");
        }
    }

    public static class ValidationException extends ExpenseTrackerException {
        public ValidationException(String message) {
            super(message, "VALIDATION_ERROR");
        }
    }

    public static class DuplicateEntityException extends ExpenseTrackerException {
        public DuplicateEntityException(String entity, String field, Object value) {
            super(String.format("%s already exists with %s: %s", entity, field, value), "DUPLICATE");
        }
    }

    public static class DataInconsistencyException extends ExpenseTrackerException {
        public DataInconsistencyException(String message) {
            super(message, "DATA_INCONSISTENCY");
        }
    }
}