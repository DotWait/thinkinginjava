package com.dotwait.collection;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

public class Hello {
    public static void main(String[] args) throws Exception {
        Method method = Hello.class.getMethod("test", Map.Entry.class);
        Type[] types = method.getGenericParameterTypes();
        for (Type type : types) {
            ParameterizedType pType = (ParameterizedType) type;
            System.out.println(pType + " ★ " + pType.getOwnerType() + " ★ " + pType.getRawType() //
                    + " ★ " + Arrays.toString(pType.getActualTypeArguments()));
            //java.util.Map.java.util.Map$Entry<T, U> ★ interface java.util.Map ★ interface java.util.Map$Entry ★ [T, U]
        }
    }

    public static <T, U> void test(Map.Entry<T, U> mapEntry) {
    }
}
