package com.skytakeaway.server.aspect;

import com.skytakeaway.common.constant.AutoFillConstant;
import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.common.enumeration.OperationType;
import com.skytakeaway.server.annotation.AutoFill;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

//Auto fill create time, update time, create user, update user
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.skytakeaway.server.mapper.*.*(..)) && @annotation(com.skytakeaway.server.annotation.AutoFill))")
    public void autoFillCutPoint(){}

    @Before("autoFillCutPoint()")
    public void autoFill(JoinPoint joinPoint){
        // Get the method signature from join point
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // Get the AutoFill annotation from the method
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // Get the operation type (INSERT, UPDATE)
        OperationType operationType = autoFill.value();

        // Get arguments of the method (Employee, Category, etc.)
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        LocalDateTime now = LocalDateTime.now();
        //get current user id with local thread
        Long currentId = BaseContext.getCurrentId();

        //give value to create time, update time, create user, create time
        if(operationType == OperationType.INSERT){
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method serCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity, now);
                serCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if (operationType == OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
