package com.dotwait.innerclass;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class EntityClass<T, ID extends Serializable> {
    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public void init(){
        entityClass = (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void print(){
        init();
        System.out.println(entityClass.getName());
    }

}
