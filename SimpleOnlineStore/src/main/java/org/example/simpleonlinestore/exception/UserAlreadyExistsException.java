package org.example.simpleonlinestore.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String mssg){
        super(mssg);
    }
}
