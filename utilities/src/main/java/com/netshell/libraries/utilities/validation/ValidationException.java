package com.netshell.libraries.utilities.validation;

public class ValidationException extends Exception {

    private final ValidationResult validationResult;

    public ValidationException() {
        validationResult = new ValidationResult();
    }

    public ValidationException(String message) {
        super(message);
        validationResult = new ValidationResult(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        validationResult = new ValidationResult(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
        validationResult = new ValidationResult();
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        validationResult = new ValidationResult(message);
    }

    public static ValidationResult.ValidationResultItem newItemInstance(String key, String value, String code, String message) {
        return new ValidationResult.ValidationResultItem(key, value, message, code);
    }

    public static ValidationResult.ValidationResultItem newItemInstance(String key, String value, String code) {
        return new ValidationResult.ValidationResultItem(key, value, code);
    }

    public static ValidationResult.ValidationResultItem newItemInstance(String key, String value) {
        return new ValidationResult.ValidationResultItem(key, value);
    }

    public static ValidationResult.ValidationResultItem newItemInstance(String key) {
        return new ValidationResult.ValidationResultItem(key);
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void add(final ValidationResult.ValidationResultItem item) {
        validationResult.addValidationResultItem(item);
    }
}
