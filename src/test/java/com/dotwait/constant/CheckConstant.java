package com.dotwait.constant;

import java.util.*;

public class CheckConstant {

    public static final int NUMBER_ASCII = 48;
    public static final int UPPER_LETTER_ASCII = 65;
    public static final int LOWER_LETTER_ASCII = 97;
    /**
     * 类型的simpleName和类型的映射关系
     */
    public static Map<String, Class> classMap;

    /**
     * 默认的方法名
     */
    public static List<String> defaultMethodName;

    /**
     * 类型和Limit注解的方法名的映射关系
     */
    public static Map<Class, List<String>> methodNameMap;

    /**
     * 需要加入映射关系的类型
     */
    public static final Class[] classArr = new Class[]{
            byte.class,
            boolean.class,
            short.class,
            char.class,
            int.class,
            long.class,
            float.class,
            double.class,
            Byte.class,
            Boolean.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class,
            List.class,
            byte[].class,
            boolean[].class,
            short[].class,
            char[].class,
            int[].class,
            long[].class,
            float[].class,
            double[].class,
            Byte[].class,
            Boolean[].class,
            Short[].class,
            Character[].class,
            Integer[].class,
            Long[].class,
            Float[].class,
            Double[].class,
            String[].class,
    };

    /**
     * 默认的方法名
     */
    public static final String[] defaultMethodNames = new String[]{"equals", "toString", "hashCode", "annotationType"};

    /**
     * 初始化classMap
     */
    static {
        defaultMethodName = new ArrayList<>(Arrays.asList(defaultMethodNames));

        classMap = new HashMap<>();
        methodNameMap = new HashMap<>();

        for (Class cls : classArr) {
            classMap.put(cls.getSimpleName(), cls);
        }

        ArrayList<String> strings = new ArrayList<>(Arrays.asList("strValue", "prefix", "suffix", "length", "randomType"));
        ArrayList<String> ints = new ArrayList<>(Arrays.asList("intValue", "intUpperLimit", "intLowerLimit"));
        ArrayList<String> longs = new ArrayList<>(Arrays.asList("longValue", "longUpperLimit", "longLowerLimit"));
        ArrayList<String> floats = new ArrayList<>(Arrays.asList("floatValue", "floatUpperLimit", "floatLowerLimit"));
        ArrayList<String> doubles = new ArrayList<>(Arrays.asList("doubleValue", "doubleUpperLimit", "doubleLowerLimit"));
        ArrayList<String> booleans = new ArrayList<>(Arrays.asList("booleanValue", "booleanRandom"));
        ArrayList<String> chars = new ArrayList<>(Arrays.asList("charValue"));
        ArrayList<String> lists = new ArrayList<>(Arrays.asList("size"));
        ArrayList<String> others = new ArrayList<>(Arrays.asList("isRequired"));

        methodNameMap.put(boolean.class, booleans);
        methodNameMap.put(Boolean.class, booleans);
        methodNameMap.put(boolean[].class, lists);
        methodNameMap.put(Boolean[].class, lists);
        methodNameMap.put(char.class, chars);
        methodNameMap.put(Character.class, chars);
        methodNameMap.put(char[].class, lists);
        methodNameMap.put(Character[].class, lists);
        methodNameMap.put(int.class, ints);
        methodNameMap.put(Integer.class, ints);
        methodNameMap.put(int[].class, lists);
        methodNameMap.put(Integer[].class, lists);
        methodNameMap.put(long.class, longs);
        methodNameMap.put(Long.class, longs);
        methodNameMap.put(long[].class, lists);
        methodNameMap.put(Long[].class, lists);
        methodNameMap.put(float.class, floats);
        methodNameMap.put(Float.class, floats);
        methodNameMap.put(float[].class, lists);
        methodNameMap.put(Float[].class, lists);
        methodNameMap.put(double.class, doubles);
        methodNameMap.put(Double.class, doubles);
        methodNameMap.put(double[].class, lists);
        methodNameMap.put(Double[].class, lists);
        methodNameMap.put(String.class, strings);
        methodNameMap.put(String[].class, lists);
        methodNameMap.put(List.class, lists);
        methodNameMap.put(Object.class, others);
    }
}
