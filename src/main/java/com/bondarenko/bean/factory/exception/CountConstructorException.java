package com.bondarenko.bean.factory.exception;

public class CountConstructorException extends RuntimeException {
    private String message;

    public CountConstructorException(String message) {
        this.message = message;
    }
}
