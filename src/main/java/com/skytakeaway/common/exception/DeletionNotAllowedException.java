package com.skytakeaway.common.exception;

public class DeletionNotAllowedException extends BaseException{
    public DeletionNotAllowedException(){
    }

    public DeletionNotAllowedException(String message){
        super(message);
    }
}
