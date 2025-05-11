package com.skytakeaway.common.exception;

public class AccountLockException extends BaseException{
    public AccountLockException() {
    }

    public AccountLockException(String message) {
        super(message);
    }
}
