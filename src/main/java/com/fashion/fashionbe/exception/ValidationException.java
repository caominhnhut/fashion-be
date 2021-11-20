package com.fashion.fashionbe.exception;

public class ValidationException extends Exception{

    public ValidationException(String errorMessage){
        super(errorMessage);
    }
}
