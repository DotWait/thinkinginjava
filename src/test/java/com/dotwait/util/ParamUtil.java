package com.dotwait.util;

import com.dotwait.annotation.FieldLimit;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParamUtil {
    private Random random = new Random();

    @Test
    public void paramTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Header header = new Header();
        header.setKey("key");
        header.setValue("value");

        Happy happy = new Happy(1, "mike", 2);
        List<Object> objects = generateObject(happy, parseFieldLimit(happy));
        List<String> json = beanToJson(objects);
        json.forEach(System.out::println);
    }

    /**
     * 生成包含不同值的对象
     * @param t 对象
     * @param lists 包含正确和错误的值列表，第一个为正确的，第二个是错误的
     * @return 不同值的对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public List<Object> generateObject(Object t, @Nullable List<List<Object>> lists) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("the length of fields is 0");
            return null;
        }
        Constructor<?>[] constructors = tClass.getConstructors();
        Constructor allParameterConstructor = null;
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
            if (constructor.getParameterCount() == fields.length) {
                allParameterConstructor = constructor;
                break;
            }
        }
        if (allParameterConstructor == null) {
            System.out.println("can not find a constructor which has all parameters");
            return null;
        }
        List<Object> result = new ArrayList<>();
        List<List<Object>> params;
        if (lists != null) {
            params = generateParams(t, lists);
        } else {
            params = generateParams(t);
        }
        assert params != null;
        for (List<Object> objects : params) {
            System.out.println(objects.size() + " , " + objects);
            result.add(allParameterConstructor.newInstance(objects.toArray()));
        }
        result.forEach(System.out::println);
        return result;
    }

    /**
     * 生成多种参数组合
     * @param t 对象
     * @return 不同参数的组合，但错误的值只有null
     */
    private List<List<Object>> generateParams(Object t) {
        Class<?> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("the length of fields is 0");
            return null;
        }

        List<Object> params = new ArrayList<>(fields.length);
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                params.add(field.getName());
            } else if (field.getType().equals(Integer.class)) {
                params.add(1);
            } else if (field.getType().equals(Long.class)) {
                params.add(2);
            } else {
                System.out.println(field.getName() + " is set null");
                params.add(null);
            }
        }
        List<List<Object>> result = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            List<Object> temp = new ArrayList<>(params);
            temp.set(i, null);
            result.add(temp);
        }
        return result;
    }

    /**
     * 生成多种参数组合
     * @param t 对象
     * @param lists 包含正确和错误的值列表，第一个为正确的，第二个是错误的
     * @return 不同参数的组合
     */
    private List<List<Object>> generateParams(Object t, List<List<Object>> lists) {
        assert lists != null;
        Class<?> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("the length of fields is 0");
            return null;
        }

        List<Object> rightList = lists.get(0);
        List<Object> errorList = lists.get(1);

        List<Object> params = new ArrayList<>(fields.length);
        List<Object> rightParams = new ArrayList<>();
        List<Object> errorParams = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType().equals(String.class)) {
                params.add(rightList.get(i));
                rightParams.add(rightList.get(i));
                errorParams.add(errorList.get(i));
            } else if (field.getType().equals(Integer.class)) {
                params.add(rightList.get(i));
                rightParams.add(rightList.get(i));
                errorParams.add(errorList.get(i));
            } else {
                System.out.println(field.getName() + " is set null");
                params.add(rightList.get(i));
                rightParams.add(rightList.get(i));
                errorParams.add(errorList.get(i));
            }
        }
        List<List<Object>> result = new ArrayList<>();
        result.add(rightParams);
        result.add(errorParams);
        for (int i = 0; i < fields.length; i++) {
            List<Object> temp = new ArrayList<>(params);
            temp.set(i, null);
            result.add(temp);
        }
        for (int i = 0; i < fields.length; i++) {
            List<Object> temp = new ArrayList<>(params);
            temp.set(i, errorList.get(i));
            result.add(temp);
        }
        return result;
    }

    /**
     * java对象转json，忽略value为null的字段
     *
     * @param objects java对象集合
     * @return json字符串集合
     */
    private List<String> beanToJson(List<Object> objects) {
        List<String> result = new ArrayList<>(objects.size());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        for (Object obj : objects) {
            try {
                result.add(objectMapper.writeValueAsString(obj));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Test
    public void t1() {
        Happy happy = new Happy(1, "mike", 2);
        List<List<Object>> lists = parseFieldLimit(happy);
        lists.forEach(list -> list.forEach(System.out::println));
    }

    /**
     * 解析FieldLimit注解，分析每个字段的限制，给出正确和错误的值
     * @param obj 对象
     * @return 包含正确和错误的值列表，第一个为正确的，第二个是错误的
     */
    private List<List<Object>> parseFieldLimit(Object obj) {
        List<List<Object>> result = new ArrayList<>();
        List<Object> rightList = new ArrayList<>();
        List<Object> errorList = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        System.out.println(fields.length);
        for (Field field : fields) {
            System.out.println(field.getName());
            FieldLimit annotation = field.getAnnotation(FieldLimit.class);
            if (annotation != null) {
                if (field.getType().equals(Integer.class)) {
                    String numberLimit = annotation.numberLimit();
                    String[] split = numberLimit.split("-");
                    Integer rightNum = 0;
                    Integer errorNum = 0;
                    if (split.length == 1) {
                        rightNum = random.nextInt(10000) + Integer.parseInt(split[0]);
                        errorNum = -random.nextInt(10000) - Integer.parseInt(split[0]);
                    } else if (split.length == 2) {
                        if ("".equals(split[0])) {
                            rightNum = random.nextInt(Integer.parseInt(split[1])) - random.nextInt(10);
                            errorNum = random.nextInt(Integer.parseInt(split[1])) + Integer.parseInt(split[1]);
                        } else {
                            rightNum = random.nextInt(Integer.parseInt(split[1]) - Integer.parseInt(split[0])) + Integer.parseInt(split[0]);
                            if (random.nextInt() % 2 == 0) {
                                errorNum = Integer.parseInt(split[0]) - random.nextInt(100);
                            } else {
                                errorNum = Integer.parseInt(split[1]) + random.nextInt(100);
                            }
                        }
                    } else {
                        System.out.println("the format of the numberLimit is not right");
                    }
                    rightList.add(rightNum);
                    errorList.add(errorNum);
                } else if (field.getType().equals(String.class)) {
                    StringBuilder rightStr = new StringBuilder();
                    String errorStr = "";
                    String prefix = annotation.stringPrefix();
                    String suffix = annotation.stringSuffix();
                    int length = annotation.stringLength();
                    System.out.println("length:"+length);
                    if (!"".equals(prefix)) {
                        rightStr.insert(0, prefix);
                    }
                    if (!"".equals(suffix)) {
                        int suffixLen = suffix.length();
                        int strLen = rightStr.length();
                        if (length != 0) {
                            int needLen = length - suffixLen - strLen;
                            for (int i = 0; i < needLen; i++) {
                                rightStr.append(random.nextInt(10));
                            }
                        }
                        rightStr.append(suffix);
                    }else if (length != 0){
                        int needLen = length - prefix.length();
                        for (int i = 0; i < needLen; i++) {
                            rightStr.append(random.nextInt(10));
                        }
                    }
                    rightList.add(rightStr.toString());
                    errorStr += rightStr + "A";
                    errorList.add(errorStr);
                } else {
                    rightList.add(null);
                    errorList.add(null);
                }
            } else {
                if (field.getType().equals(String.class)) {
                    rightList.add(random.nextInt(100) + "");
                    errorList.add(random.nextInt(100) + "");
                } else if (field.getType().equals(Integer.class)) {
                    rightList.add(random.nextInt(100));
                    errorList.add(random.nextInt(100));
                } else {
                    rightList.add(null);
                    errorList.add(null);
                }
            }
        }
        result.add(rightList);
        result.add(errorList);
        return result;
    }
}
