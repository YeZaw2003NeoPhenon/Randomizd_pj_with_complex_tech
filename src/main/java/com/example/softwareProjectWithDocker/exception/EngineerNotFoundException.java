package com.example.softwareProjectWithDocker.exception;


public class EngineerNotFoundException extends RuntimeException{
    public EngineerNotFoundException(String message){
        super(message);
    }

}
