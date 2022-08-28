package com.kakjziblog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class KakjziblogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();
    public KakjziblogException(String message) {
        super(message);
    }

    public KakjziblogException(String message, Throwable cause) {
        super(message, cause);
    }
    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
