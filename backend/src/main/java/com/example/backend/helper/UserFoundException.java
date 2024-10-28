package com.example.backend.helper;


public class UserFoundException extends Exception{
    public UserFoundException() {
        super("User with this User name is already is already in DB !! try with anther one");
    }

    public UserFoundException(String msg) {
        super(msg);
    }
}