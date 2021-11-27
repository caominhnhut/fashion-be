package com.fashion.fashionbe.enumeration;

public enum FieldName{

    userName("The username should not be empty"),
    password("The password should not be empty"),
    roles("List roles of user should not be empty");

    private String message;

    FieldName(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
