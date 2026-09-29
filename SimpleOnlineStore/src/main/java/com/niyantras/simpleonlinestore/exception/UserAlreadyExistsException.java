package com.niyantras.simpleonlinestore.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String mssg){
        super(mssg);
    }
}
