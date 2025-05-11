package com.skytakeaway.common.context;

//use to save the id of current user
public class BaseContext {
    public static ThreadLocal<Long> currentId = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        currentId.set(id);
    }

    public static Long getCurrentId(){
        return currentId.get();
    }

    public static void removeCurrentId(){
        currentId.remove();
    }
}
