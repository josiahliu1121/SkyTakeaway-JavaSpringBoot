package com.skytakeaway.common.exception;

public class AddressIsEmptyException extends BaseException{
    public AddressIsEmptyException(){
    }

    public AddressIsEmptyException (String message){
        super(message);
    }
}
