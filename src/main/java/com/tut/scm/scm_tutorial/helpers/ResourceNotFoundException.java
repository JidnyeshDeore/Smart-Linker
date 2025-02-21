package com.tut.scm.scm_tutorial.helpers;


public class ResourceNotFoundException extends  RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
