package com.byunmall.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ShoppingMallException extends RuntimeException{

    private final Map<String, String> validation = new HashMap<>();

    public ShoppingMallException(String message) {
        super(message);
    }

    public ShoppingMallException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName,message);
    }
}
