package com.skytakeaway.server.handler;

import com.skytakeaway.common.exception.BaseException;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.common.constant.MessageConstant;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result exceptionHandler(BaseException baseException){
        return Result.error(baseException.getMessage());
    }

    @ExceptionHandler()
    public Result exception(org.springframework.dao.DuplicateKeyException duplicateKeyException){
        duplicateKeyException.printStackTrace();
        String message = duplicateKeyException.getMessage();
        if(message.contains("Duplicate entry")){
            return Result.error(MessageConstant.USERNAME_ALREADY_EXIST);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
