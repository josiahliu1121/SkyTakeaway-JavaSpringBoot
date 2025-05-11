package com.skytakeaway.server.annotation;

import com.skytakeaway.common.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//to mark method that need to fill in same content in table (create time, update time, create user, update user)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //UPDATE INSERT
    OperationType value();
}
