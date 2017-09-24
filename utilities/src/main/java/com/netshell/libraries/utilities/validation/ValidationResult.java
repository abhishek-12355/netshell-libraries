package com.netshell.libraries.utilities.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationResult implements Serializable {

    private String message;
    private List<ValidationResultItem> validationResultItemList = new ArrayList<>();

    ValidationResult() {
    }

    ValidationResult(String message) {
        this.message = message;
    }

    public List<ValidationResultItem> getValidationResultItemList() {
        return Collections.unmodifiableList(validationResultItemList);
    }

    public void setValidationResultItemList(List<ValidationResultItem> validationResultItemList) {
        this.validationResultItemList = validationResultItemList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    void addValidationResultItem(final ValidationResultItem item) {
        validationResultItemList.add(item);
    }

    public Set<String> getCodes() {
        if (validationResultItemList.isEmpty()) {
            return Collections.emptySet();
        }

        return validationResultItemList.stream()
                .map(validationResultItem -> validationResultItem.code)
                .collect(Collectors.toSet());
    }

    public Set<String> getKeys() {
        if (validationResultItemList.isEmpty()) {
            return Collections.emptySet();
        }

        return validationResultItemList.stream()
                .map(validationResultItem -> validationResultItem.key)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "validationResultItemList=" + validationResultItemList +
                '}';
    }

    public static final class ValidationResultItem implements Serializable {
        private String key;
        private String code;
        private String message;
        private String value;

        public ValidationResultItem(String key, String value, String code, String message) {
            this.key = key;
            this.code = code;
            this.message = message;
            this.value = value;
        }

        public ValidationResultItem(String key, String value, String code) {
            this(key, value, code, null);
        }

        public ValidationResultItem(String key, String value) {
            this(key, value, null, null);
        }

        public ValidationResultItem(String key) {
            this(key, null, null, null);
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ValidationResultItem{" +
                    "key='" + key + '\'' +
                    ", code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
