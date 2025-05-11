package com.skytakeaway.common.exception;

public class ShoppingCartEmpty extends BaseException {
    public ShoppingCartEmpty(){}

    public ShoppingCartEmpty(String message){
        super(message);
    }
}
