package com.dotwait.check;

import cn.hutool.core.bean.BeanUtil;
import com.dotwait.annotation.Limit;
import com.dotwait.constitute.Constitute;
import com.dotwait.enums.RandomType;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.dotwait.constant.CheckConstant.*;

/**
 * generate different parameters
 */
public class CheckUtil {
    private static final Random random = new Random();

    private static Stack<Integer> stack = new Stack<>();

    private static Map<String, Object> rightMap = new HashMap<>();

    private static Map<String, Object> errorMap = new HashMap<>();

    /**
     * 根据不同的组合生成不同字段为null的对象
     *
     * @param cls    class对象
     * @param oldObj 参照对象
     * @return 不同字段为null的对象集合
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static List<Object> generateNullFieldObjects(Class cls, Object oldObj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Field> fields = getLimitAnnotationFields(cls);
        List<Object> result = new ArrayList<>();
        Constitute constitute = new Constitute();
        //初始化数字序列
        List<Integer> numbers = constitute.generateNumbers(fields.size());
        //生成数字序列的所有组合
        List<List<Integer>> combinations = constitute.allCombination(numbers);
        for (List<Integer> combination : combinations) {
            List<Field> fieldsMapToCombination = getFieldsMapToCombination(fields, combination);
            Object newObj = generateNullFieldObject(cls, oldObj, fieldsMapToCombination);
            result.add(newObj);
        }
        return result;
    }

    /**
     * 根据组合生成对应的错误对象
     * @param cls Class对象
     * @param oldObj 参照对象
     * @return 错误对象集合
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static List<Object> generateErrorFieldObjects(Class cls, Object oldObj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Field> fields = getLimitAnnotationFields(cls);
        List<Object> result = new ArrayList<>();
        Constitute constitute = new Constitute();
        /*去除errorMap中值为null的字段*/
        removeValueIsNull();
        List<LayerToFieldValue> layerToFieldValues = mapToList(errorMap);
        //初始化数字序列
        List<Integer> numbers = constitute.generateNumbers(layerToFieldValues.size());
        //生成数字序列的所有组合
        List<List<Integer>> combinations = constitute.allCombination(numbers);
        for (List<Integer> combination : combinations) {
            List<LayerToFieldValue> layerToFieldValueMapToCombination = getLayerToFieldValueMapToCombination(layerToFieldValues, combination);
            Object newObj = generateErrorFieldObject(cls, oldObj, layerToFieldValueMapToCombination);
            result.add(newObj);
        }
        return result;
    }

    /**
     * 生成错误对象
     * @param cls Class对象
     * @param oldObj 参照对象
     * @param layerToFieldValues 层级字段值
     * @return 包含错误值的对象
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private static Object generateErrorFieldObject(Class cls, Object oldObj, List<LayerToFieldValue> layerToFieldValues) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object newObj = cls.newInstance();
        BeanUtil.copyProperties(oldObj, newObj);
        List<Field> fields = getLimitAnnotationFields(cls);
        for (LayerToFieldValue layerToFieldValue : layerToFieldValues) {
            String layer = layerToFieldValue.getLayer();
            Object value = layerToFieldValue.getValue();
            List<Integer> positions = parseLayer(layer);
            PositionField positionField = new PositionField(cls, newObj, fields);
            for (Integer position : positions) {
                positionField = findFieldByPositions(positionField.getCls(),
                        position, positionField.getFields(), positionField.getObj());
            }
            String setMethodName = spliceSetMethodName(positionField.getFields().get(0));
            Method setMethod = positionField.getCls().getDeclaredMethod(setMethodName, positionField.getFields().get(0).getType());
            System.out.println(positionField);
            setMethod.invoke(positionField.getObj(), value);
        }
        return newObj;
    }

    /**
     * 根据index查询对应的字段
     * @param cls Class对象
     * @param position 字段位置
     * @param fields 字段集合
     * @param obj 参照对象
     * @return 位置字段相关信息
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private static PositionField findFieldByPositions(Class cls, Integer position, List<Field> fields, Object obj) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Field field = fields.get(position);
        Class type = field.getType();
        //判断类型是否为基础类型
        if (type.equals(classMap.get(type.getSimpleName()))) {
            String simpleName = type.getSimpleName();
            if ("List".equals(simpleName)){
                //获取List泛型
                Class listGeneric = getListGeneric(type, field);
                if (listGeneric != null){
                    String genericSimpleName = listGeneric.getSimpleName();
                    //泛型是否为基础类型
                    if (classMap.get(genericSimpleName) != null){
                        List<Field> list = new ArrayList<>();
                        list.add(field);
                        return new PositionField(cls, obj, list);
                    }else {
                        String getMethodName = spliceGetMethodName(field);
                        Method getMethod = cls.getDeclaredMethod(getMethodName);
                        List invoke = (List)getMethod.invoke(obj);
                        return new PositionField(listGeneric, invoke.get(0), getLimitAnnotationFields(listGeneric));
                    }
                }
            }
            if (simpleName.contains("[]")){
                //是否为基础类型
                if (classMap.get(removeArray(simpleName)) != null ){
                    List<Field> list = new ArrayList<>();
                    list.add(field);
                    return new PositionField(cls, obj, list);
                }else {
                    String getMethodName = spliceGetMethodName(field);
                    Method getMethod = cls.getDeclaredMethod(getMethodName);
                    Object invoke = getMethod.invoke(obj);
                    Class aClass = Class.forName(arrayToClass(type.getName()));
                    return new PositionField(aClass, invoke, getLimitAnnotationFields(aClass));
                }
            }
            List<Field> list = new ArrayList<>();
            list.add(field);
            return new PositionField(cls, obj, list);
        } else {
            String getMethodName = spliceGetMethodName(field);
            Method getMethod = cls.getDeclaredMethod(getMethodName);
            Object invoke = getMethod.invoke(obj);
            return new PositionField(type, invoke, getLimitAnnotationFields(type));
        }
    }

    /**
     * 解析每个层级，标识了每个层级字段的位置
     * @param layer 层级
     * @return 字段位置集合
     */
    private static List<Integer> parseLayer(String layer){
        String[] split = layer.split("-");
        List<Integer> result = new ArrayList<>(split.length);
        for (String s : split) {
            result.add(Integer.parseInt(s));
        }
        return result;
    }

    /**
     * 根据组合获取对应的layerToFieldValue
     * @param layerToFieldValues 位置字段值
     * @param combination 组合
     * @return layerToFieldValue集合
     */
    private static List<LayerToFieldValue> getLayerToFieldValueMapToCombination(List<LayerToFieldValue> layerToFieldValues, List<Integer> combination){
        List<LayerToFieldValue> result = new ArrayList<>();
        combination.forEach(index -> result.add(layerToFieldValues.get(index)));
        return result;
    }

    /**
     * Map<String, Object>转ArrayList<LayerToFieldValue>
     * @param map map集合
     * @return ArrayList集合
     */
    private static List<LayerToFieldValue> mapToList(Map<String, Object> map){
        return map.entrySet().stream().map(m -> new LayerToFieldValue(m.getKey(), m.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    /**
     * 去除errorMap中值为null的字段
     */
    private static void removeValueIsNull(){
        errorMap.entrySet().removeIf(next -> next.getValue() == null);
    }

    /**
     * 根据组合获取对应的Field
     *
     * @param fields      字段集合
     * @param combination 组合
     * @return 对应的字段
     */
    private static List<Field> getFieldsMapToCombination(List<Field> fields, List<Integer> combination) {
        List<Field> result = new ArrayList<>();
        combination.forEach(index -> result.add(fields.get(index)));
        return result;
    }

    /**
     * 获取有Limit注解的字段
     *
     * @param cls class对象
     * @return 有Limit注解的字段数组
     */
    private static List<Field> getLimitAnnotationFields(Class cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field field : declaredFields) {
            Limit limit = field.getAnnotation(Limit.class);
            if (limit == null) {
                continue;
            }
            fields.add(field);
        }
        return fields;
    }

    /**
     * 生成其中一个字段为null的对象集合，每个对象只有一个字段为null
     *
     * @param cls    class对象
     * @param oldObj 参照对象
     * @return 其中一个字段为null的对象集合
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static List<Object> generateOneNullFieldObjects(Class cls, Object oldObj) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = cls.getDeclaredFields();
        List<Object> objects = new ArrayList<>(fields.length);
        for (Field field : fields) {
            Limit limit = field.getAnnotation(Limit.class);
            if (limit == null) {
                System.out.println("this field has not limit annotation");
                continue;
            }
            Object newObj = cls.newInstance();
            BeanUtil.copyProperties(oldObj, newObj);
            String setMethodName = spliceSetMethodName(field);
            Method setMethod = cls.getDeclaredMethod(setMethodName, field.getType());
            setMethod.invoke(newObj, (Object) null);
            objects.add(newObj);
        }
        return objects;
    }

    /**
     * 生成指定一个或多个字段为null的对象
     *
     * @param cls    class对象
     * @param oldObj 参照对象
     * @param fields 需要为null的字段
     * @return 包含多个字段为null的对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static Object generateNullFieldObject(Class cls, Object oldObj, List<Field> fields) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object newObj = cls.newInstance();
        BeanUtil.copyProperties(oldObj, newObj);
        for (Field field : fields) {
            Limit limit = field.getAnnotation(Limit.class);
            if (limit == null) {
                System.out.println("this field has not limit annotation");
                continue;
            }
            String setMethodName = spliceSetMethodName(field);
            Method setMethod = cls.getDeclaredMethod(setMethodName, field.getType());
            setMethod.invoke(newObj, (Object) null);
        }
        return newObj;
    }

    /**
     * 打印正确的参数
     */
    public static void printRightMap() {
        System.out.println("============= print rightMap ==========");
        rightMap.entrySet().forEach(System.out::println);
    }

    /**
     * 打印错误的参数
     */
    public static void printErrorMap(){
        System.out.println("============= print errorMap ==========");
        errorMap.entrySet().forEach(System.out::println);
    }

    /**
     * 根据字段获取该字段的set方法名
     *
     * @param field 字段
     * @return set方法名
     */
    private static String spliceSetMethodName(Field field) {
        if (field == null) {
            return "";
        }
        String name = field.getName();
        String firstLetter = name.substring(0, 1);
        return "set" + firstLetter.toUpperCase() + name.substring(1);
    }

    /**
     * 根据字段获取该字段的get方法名
     *
     * @param field 字段
     * @return get方法名
     */
    private static String spliceGetMethodName(Field field) {
        if (field == null) {
            return "";
        }
        String name = field.getName();
        String firstLetter = name.substring(0, 1);
        return "get" + firstLetter.toUpperCase() + name.substring(1);
    }

    /**
     * 解析Limit注解标注的对象
     *
     * @param cls Class对象
     * @return 赋值后的对象
     * @throws Exception
     */
    public static Object parseLimit(Class cls) throws Exception {
        List<Field> fields = getLimitAnnotationFields(cls);
        Object obj = cls.newInstance();
        for (int i = 0; i < fields.size(); i++) {
            stack.push(i);
            Limit limit = fields.get(i).getAnnotation(Limit.class);
            Class type = fields.get(i).getType();
            /*是否为基础类型*/
            if (type.equals(classMap.get(type.getSimpleName()))) {
                dealBaseType(type, fields.get(i), obj, limit);
            } else {
                fields.get(i).set(obj, parseLimit(type));
            }
            stack.pop();
        }
        return obj;
    }

    /**
     * 处理基础类型的情况
     *
     * @param type  字段类型
     * @param field 字段
     * @param obj   待赋值的对象
     * @param limit limit注解对象
     * @throws Exception
     */
    private static void dealBaseType(Class type, Field field, Object obj, Limit limit) throws Exception {
        List<String> methodNames = methodNameMap.get(type);
        if (methodNames != null && methodNames.size() > 0) {
            List<Object> objects = getLimitAnnotationParams(methodNames, limit);
            field.setAccessible(true);
            /*基本类型+String*/
            Object value = baseType(type, objects);
            if (value != null) {
                if (value instanceof FieldValue){
                    FieldValue fieldValue = (FieldValue) value;
                    Object rightValue = fieldValue.getRightValue();
                    Object errorValue = fieldValue.getErrorValue();
                    field.set(obj, rightValue);
                    rightMap.put(spliceStack(), rightValue);
                    errorMap.put(spliceStack(), errorValue);
                }
                return;
            }
            String simpleName = type.getSimpleName();
            /*list*/
            if ("List".equals(simpleName)) {
                List list = dealListType(type, field, objects, limit);
                List<Object> rightList = new ArrayList<>(list.size());
                List<Object> errorList = new ArrayList<>(list.size());
                list.forEach(object -> {
                    if (object instanceof FieldValue){
                        FieldValue fieldValue = (FieldValue) object;
                        rightList.add(fieldValue.getRightValue());
                        errorList.add(fieldValue.getErrorValue());
                    }
                });
                if (list.get(0) instanceof FieldValue){
                    field.set(obj, rightList);
                    rightMap.put(spliceStack(), rightList);
                    errorMap.put(spliceStack(), errorList);
                }else {
                    field.set(obj, list);
                    rightMap.put(spliceStack(), list);
                }
                return;
            }
            /*array*/
            if (simpleName.contains("[]")) {
                Object arrayValue = dealArrayType(type, simpleName, objects, limit);
                if (arrayValue instanceof FieldValue){
                    FieldValue fieldValue = (FieldValue) arrayValue;
                    Object rightArray = fieldValue.getRightValue();
                    Object errorArray = fieldValue.getErrorValue();
                    field.set(obj, rightArray);
                    rightMap.put(spliceStack(), rightArray);
                    errorMap.put(spliceStack(), errorArray);
                }
            }
        }
    }

    /**
     * 拼接对象字段位置
     *
     * @return 包含字段位置的字符串
     */
    private static String spliceStack() {
        List<Integer> list = new ArrayList<>(stack);
        StringBuilder result = new StringBuilder();
        if (list.size() > 0) {
            for (Integer integer : list) {
                result.append("-").append(integer);
            }
            return result.substring(1);
        }
        return result.toString();
    }

    /**
     * 从Limit注解中获取参数值
     *
     * @param methodNames 方法名称集合
     * @param limit       注解对象
     * @return 参数值
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static List<Object> getLimitAnnotationParams(List<String> methodNames, Limit limit) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Limit> limitClass = Limit.class;
        List<Object> objects = new ArrayList<>(methodNames.size());
        for (String methodName : methodNames) {
            Method method = limitClass.getDeclaredMethod(methodName);
            objects.add(method.invoke(limit));
        }
        return objects;
    }

    /**
     * 处理类型为Array的字段
     *
     * @param type       字段类型
     * @param simpleName 简单字段类型名称
     * @param objects    参数值
     * @return array对象，包含了对应的随机值
     * @throws Exception
     */
    private static Object dealArrayType(Class type, String simpleName, List<Object> objects, Limit limit) throws Exception {
        int size = getListSize((int) objects.get(0));
        String s = arrayToClass(type.getName());
        System.out.println(s);
        Class aClass = Class.forName(arrayToClass(type.getName()));
        Object rightArray = Array.newInstance(aClass, size);
        Object errorArray = Array.newInstance(aClass, size);
        /*是否为基础类型*/
        if (classMap.get(removeArray(simpleName)) != null) {
            List<String> methodNames = methodNameMap.get(aClass);
            if (methodNames != null && methodNames.size() > 0) {
                List<Object> childObjects = getLimitAnnotationParams(methodNames, limit);
                childObjects.forEach(System.out::println);
                for (int i = 0; i < size; i++) {
                    Object value = baseType(removeArray(simpleName), childObjects);
                    if (value instanceof FieldValue){
                        FieldValue fieldValue = (FieldValue) value;
                        Object rightValue = fieldValue.getRightValue();
                        Object errorValue = fieldValue.getErrorValue();
                        Array.set(rightArray, i, rightValue);
                        Array.set(errorArray, i, errorValue);
                    }
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                Array.set(rightArray, i, parseLimit(aClass));
            }
        }
        return new FieldValue(rightArray, errorArray);
    }

    /**
     * 将数组类型字符串转换为单个元素类型字符串
     *
     * @param arrayName 数组类型字符串
     * @return 单个元素类型字符串
     */
    private static String arrayToClass(String arrayName) {
        if (arrayName.contains("[L")) {
            String str = arrayName.substring(arrayName.indexOf("[L") + 2);
            return str.substring(0, str.indexOf(";"));
        }
        return arrayName;
    }

    /**
     * 处理类型List的字段
     *
     * @param type    字段类型
     * @param field   字段
     * @param objects 参数值
     * @return list对象，包含了对应的随机值
     * @throws Exception
     */
    private static List dealListType(Class type, Field field, List<Object> objects, Limit limit) throws Exception {
        List list = new ArrayList();
        int size = getListSize((int) objects.get(0));
        Class listGeneric = getListGeneric(type, field);
        if (listGeneric != null) {
            String simpleNameGeneric = listGeneric.getSimpleName();
            /*是否为自定义对象,不支持List<List<String>>嵌套*/
            if (classMap.get(simpleNameGeneric) != null) {
                List<String> methodNames = methodNameMap.get(listGeneric);
                if (methodNames != null && methodNames.size() > 0) {
                    List<Object> genericObjects = getLimitAnnotationParams(methodNames, limit);
                    for (int i = 0; i < size; i++) {
                        list.add(baseType(listGeneric, genericObjects));
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    list.add(parseLimit(listGeneric));
                }
            }
        }
        return list;
    }


    /**
     * 获取List的泛型
     *
     * @param type  类型
     * @param field 字段
     * @return 泛型的类型
     */
    private static Class getListGeneric(Class type, Field field) throws ClassNotFoundException {
        if (type.equals(List.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Type[] actualTypeArguments = genericType.getActualTypeArguments();
            System.out.println(actualTypeArguments.length);
            for (Type argument : actualTypeArguments) {
                return Class.forName(argument.getTypeName());
            }
        }
        return null;
    }

    /**
     * 基础类型（非自定义，非数组和集合）
     *
     * @param type    字段类型
     * @param objects 参数值
     * @return 随机值
     */
    private static Object baseType(Class type, List<Object> objects) {
        return baseType(type.getSimpleName(), objects);
    }

    /**
     * 基础类型（非自定义，非数组和集合）
     * 生成正确的随机值和错误的随机值
     *
     * @param typeSimpleName 字段类型简单名称
     * @param objects        参数值
     * @return 返回随机值对象，包含一个正确值和一个错误值
     */
    private static Object baseType(String typeSimpleName, List<Object> objects) {
        /*String*/
        if ("String".equals(typeSimpleName)) {
            String rightString = spliceString(String.valueOf(objects.get(0)),
                    String.valueOf(objects.get(1)), String.valueOf(objects.get(2)), (int) objects.get(3), (RandomType) objects.get(4));
            String errorString = spliceErrorString(String.valueOf(objects.get(0)),
                    String.valueOf(objects.get(1)), String.valueOf(objects.get(2)), (int) objects.get(3), (RandomType) objects.get(4));
            return new FieldValue(rightString, errorString);
        }
        /*base data*/
        if ("int".equals(typeSimpleName) || "Integer".equals(typeSimpleName)) {
            int rightInt = generateRandomInt((int) objects.get(0), (int) objects.get(1), (int) objects.get(2));
            Integer errorInt = generateRandomErrorInt((int) objects.get(0), (int) objects.get(1), (int) objects.get(2));
            return new FieldValue(rightInt, errorInt);
        }
        if ("long".equals(typeSimpleName) || "Long".equals(typeSimpleName)) {
            long rightLong = generateRandomLong((long) objects.get(0), (long) objects.get(1), (long) objects.get(2));
            Long errorLong = generateRandomErrorLong((long) objects.get(0), (long) objects.get(1), (long) objects.get(2));
            return new FieldValue(rightLong, errorLong);
        }
        if ("float".equals(typeSimpleName) || "Float".equals(typeSimpleName)) {
            float rightFloat = generateRandomFloat((float) objects.get(0), (float) objects.get(1), (float) objects.get(2));
            Float errorFloat = generateRandomErrorFloat((float) objects.get(0), (float) objects.get(1), (float) objects.get(2));
            return new FieldValue(rightFloat, errorFloat);
        }
        if ("double".equals(typeSimpleName) || "Double".equals(typeSimpleName)) {
            double rightDouble = generateRandomDouble((double) objects.get(0), (double) objects.get(1), (double) objects.get(2));
            Double errorDouble = generateRandomErrorDouble((double) objects.get(0), (double) objects.get(1), (double) objects.get(2));
            return new FieldValue(rightDouble, errorDouble);
        }
        if ("char".equals(typeSimpleName) || "Character".equals(typeSimpleName)) {
            char rightChar = generateRandomChar((char) objects.get(0));
            return new FieldValue(rightChar, null);
        }
        if ("boolean".equals(typeSimpleName) || "Boolean".equals(typeSimpleName)) {
            boolean rightBoolean = generateRandomBoolean((boolean) objects.get(0), (boolean) objects.get(1));
            return new FieldValue(rightBoolean, null);
        }
        return null;
    }

    /**
     * 去除[]后的字符串
     *
     * @param name 类型名称字符串
     * @return 去除[]后的字符串
     */
    private static String removeArray(String name) {
        if (name.contains("[]")) {
            return name.substring(0, name.indexOf("["));
        }
        return name;
    }

    /**
     * 根据上下限产生指定范围的数字
     *
     * @param intValue      指定值
     * @param intUpperLimit 指定上限
     * @param intLowerLimit 指定下限
     * @return 随机数
     */
    private static int generateRandomInt(int intValue, int intUpperLimit, int intLowerLimit) {
        if (intLowerLimit > intUpperLimit || intValue != Integer.MIN_VALUE) {
            return intValue;
        }
        return random.nextInt(intUpperLimit + intLowerLimit) - intLowerLimit;
    }

    /**
     * 根据上下限产生不在指定范围的int数字，如果指定值或为指定上下限，则无法产生错误数字
     *
     * @param intValue      指定值
     * @param intUpperLimit 指定上限
     * @param intLowerLimit 指定下限
     * @return 错误int数字
     */
    private static Integer generateRandomErrorInt(int intValue, int intUpperLimit, int intLowerLimit) {
        if (intLowerLimit > intUpperLimit || intValue != Integer.MIN_VALUE ||
                (intUpperLimit == Integer.MAX_VALUE && intLowerLimit == Integer.MIN_VALUE)) {
            return null;
        }
        if (intUpperLimit == Integer.MAX_VALUE) {
            return Integer.MIN_VALUE;
        } else if (intLowerLimit == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return random.nextInt(2) == 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
    }

    /**
     * 根据上下限产生指定范围的数字
     *
     * @param longValue      指定值
     * @param longUpperLimit 指定上限
     * @param longLowerLimit 指定下限
     * @return 随机数
     */
    private static long generateRandomLong(long longValue, long longUpperLimit, long longLowerLimit) {
        if (longLowerLimit > longUpperLimit || longValue != Long.MIN_VALUE) {
            return longValue;
        }
        long range = longUpperLimit - longLowerLimit;
        int bound = Integer.MAX_VALUE;
        if (range < Integer.MAX_VALUE) {
            bound = (int) range;
        }
        return random.nextInt(bound) + longLowerLimit;
    }

    /**
     * 根据上下限产生不在指定范围的long数字，如果指定值或为指定上下限，则无法产生错误数字
     *
     * @param longValue      指定值
     * @param longUpperLimit 指定上限
     * @param longLowerLimit 指定下限
     * @return 错误long数字
     */
    private static Long generateRandomErrorLong(long longValue, long longUpperLimit, long longLowerLimit) {
        if (longLowerLimit > longUpperLimit || longValue != Long.MIN_VALUE ||
                (longUpperLimit == Long.MAX_VALUE && longLowerLimit == Long.MIN_VALUE)) {
            return null;
        }
        if (longUpperLimit == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        } else if (longLowerLimit == Long.MIN_VALUE) {
            return Long.MAX_VALUE;
        } else {
            return random.nextInt(2) == 0 ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
    }

    /**
     * 只指定了下限的情况，生成错误的long值
     *
     * @param longLowerLimit 指定下限
     * @return 错误数字
     */
    private static Long onlyLowerLimit(long longLowerLimit) {
        if (longLowerLimit - Long.MIN_VALUE >= Integer.MAX_VALUE) {
            return longLowerLimit - random.nextInt(Integer.MAX_VALUE);
        } else {
            return longLowerLimit - random.nextInt((int) (longLowerLimit - Long.MIN_VALUE));
        }
    }

    /**
     * 只指定了上限的情况，生成错误的long值
     *
     * @param longUpperLimit 指定上限
     * @return 错误数字
     */
    private static Long onlyUpperLimit(long longUpperLimit) {
        if (Long.MAX_VALUE - longUpperLimit >= Integer.MAX_VALUE) {
            return longUpperLimit + random.nextInt(Integer.MAX_VALUE);
        } else {
            return longUpperLimit + random.nextInt((int) (Long.MIN_VALUE - longUpperLimit));
        }
    }

    /**
     * 根据上下限产生指定范围的数字
     *
     * @param floatValue      指定值
     * @param floatUpperLimit 指定上限
     * @param floatLowerLimit 指定下限
     * @return 随机数
     */
    private static float generateRandomFloat(float floatValue, float floatUpperLimit, float floatLowerLimit) {
        if (floatLowerLimit > floatUpperLimit || floatValue != Float.MIN_VALUE) {
            return floatValue;
        }
        float range = floatUpperLimit - floatLowerLimit;
        int bound = Integer.MAX_VALUE;
        if (range < Integer.MAX_VALUE) {
            bound = (int) range;
        }
        return random.nextInt(bound) + floatLowerLimit;
    }

    /**
     * 根据上下限产生不在指定范围的float数字，如果指定值或为指定上下限，则无法产生错误数字
     *
     * @param floatValue      指定值
     * @param floatUpperLimit 指定上限
     * @param floatLowerLimit 指定下限
     * @return 错误的float数字
     */
    private static Float generateRandomErrorFloat(float floatValue, float floatUpperLimit, float floatLowerLimit) {
        if (floatLowerLimit > floatUpperLimit || floatValue != Float.MIN_VALUE ||
                (floatLowerLimit == Float.MIN_VALUE && floatUpperLimit == Float.MAX_VALUE)) {
            return null;
        }
        if (floatUpperLimit == Float.MAX_VALUE) {
            return Float.MIN_VALUE;
        } else if (floatLowerLimit == Float.MIN_VALUE) {
            return Float.MAX_VALUE;
        } else {
            return random.nextInt(2) == 0 ? Float.MIN_VALUE : Float.MAX_VALUE;
        }
    }

    /**
     * 只指定了下限的情况，产生错误的float值
     *
     * @param floatLowerLimit 下限
     * @return 错误的float值
     */
    private static Float onlyLowerLimit(float floatLowerLimit) {
        if (floatLowerLimit - Float.MIN_VALUE >= Integer.MAX_VALUE) {
            return floatLowerLimit - random.nextInt(Integer.MAX_VALUE);
        } else {
            return floatLowerLimit - random.nextInt((int) (floatLowerLimit - Float.MIN_VALUE));
        }
    }

    /**
     * 只指定了上限的情况，生成错误的float值
     *
     * @param floatUpperLimit 指定上限
     * @return 错误float值
     */
    private static Float onlyUpperLimit(float floatUpperLimit) {
        if (Float.MAX_VALUE - floatUpperLimit >= Integer.MAX_VALUE) {
            return floatUpperLimit + random.nextInt(Integer.MAX_VALUE);
        } else {
            return floatUpperLimit + random.nextInt((int) (Float.MIN_VALUE - floatUpperLimit));
        }
    }

    /**
     * 根据上下限产生指定范围的数字
     *
     * @param doubleValue      指定值
     * @param doubleUpperLimit 指定上限
     * @param doubleLowerLimit 指定下限
     * @return 随机数
     */
    private static double generateRandomDouble(double doubleValue, double doubleUpperLimit, double doubleLowerLimit) {
        if (doubleLowerLimit > doubleUpperLimit || doubleValue != Double.MIN_VALUE) {
            return doubleValue;
        }
        double range = doubleUpperLimit - doubleLowerLimit;
        int bound = Integer.MAX_VALUE;
        if (range < Integer.MAX_VALUE) {
            bound = (int) range;
        }
        return random.nextInt(bound) + doubleLowerLimit;
    }

    /**
     * 根据上下限产生不在指定范围的double数字，如果指定值或为指定上下限，则无法产生错误数字
     *
     * @param doubleValue      指定值
     * @param doubleUpperLimit 指定上限
     * @param doubleLowerLimit 指定下限
     * @return 错误的double值
     */
    private static Double generateRandomErrorDouble(double doubleValue, double doubleUpperLimit, double doubleLowerLimit) {
        if (doubleLowerLimit > doubleUpperLimit || doubleValue != Double.MIN_VALUE ||
                (doubleLowerLimit == Double.MIN_VALUE && doubleUpperLimit == Double.MAX_VALUE)) {
            return null;
        }
        if (doubleUpperLimit == Float.MAX_VALUE) {
            return Double.MIN_VALUE;
        } else if (doubleLowerLimit == Float.MIN_VALUE) {
            return Double.MAX_VALUE;
        } else {
            return random.nextInt(2) == 0 ? Double.MIN_VALUE : Double.MAX_VALUE;
        }
    }

    /**
     * 只指定了下限的情况，产生错误的double值
     *
     * @param doubleLowerLimit 下限
     * @return 错误的double值
     */
    private static Double onlyLowerLimit(double doubleLowerLimit) {
        if (doubleLowerLimit - Double.MIN_VALUE >= Integer.MAX_VALUE) {
            return doubleLowerLimit - random.nextInt(Integer.MAX_VALUE);
        } else {
            return doubleLowerLimit - random.nextInt((int) (doubleLowerLimit - Double.MIN_VALUE));
        }
    }

    /**
     * 只指定了上限的情况，生成错误的double值
     *
     * @param doubleUpperLimit 指定上限
     * @return 错误double值
     */
    private static Double onlyUpperLimit(double doubleUpperLimit) {
        if (Double.MAX_VALUE - doubleUpperLimit >= Integer.MAX_VALUE) {
            return doubleUpperLimit + random.nextInt(Integer.MAX_VALUE);
        } else {
            return doubleUpperLimit + random.nextInt((int) (Double.MIN_VALUE - doubleUpperLimit));
        }
    }

    /**
     * 产生随机的booelan
     *
     * @param booleanValue  指定boolean
     * @param booleanRandom 是否随机产生boolean
     * @return boolean值
     */
    private static boolean generateRandomBoolean(boolean booleanValue, boolean booleanRandom) {
        if (!booleanRandom) {
            return booleanValue;
        }
        return random.nextInt(2) == 0;
    }

    /**
     * 产生随机的一个字符
     *
     * @param charValue 指定字符
     * @return 字符
     */
    private static char generateRandomChar(char charValue) {
        if ((int) charValue != 0) {
            return charValue;
        }
        return (char) random.nextInt(256);
    }

    /**
     * 获取集合或数组的大小
     *
     * @param size 大小
     * @return 大小
     */
    private static int getListSize(int size) {
        return size;
    }

    /**
     * 拼接指定前后缀以及长度的随机字符串
     *
     * @param strValue   指定字符串的值
     * @param prefix     前缀
     * @param suffix     后缀
     * @param length     长度
     * @param randomType 随机字符串的类型
     * @return 随机字符串
     */
    private static String spliceString(String strValue, String prefix, String suffix, int length, RandomType randomType) {
        if (!"".equals(strValue)) {
            return strValue;
        }
        if (length <= prefix.length() + suffix.length()) {
            return prefix + suffix;
        }
        int middleLen = length - prefix.length() - suffix.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < middleLen; i++) {
            if (RandomType.ALL.equals(randomType)) {
                sb.append(randomCharacter());
            } else if (RandomType.NUMBER.equals(randomType)) {
                sb.append(randomNumber());
            } else if (RandomType.LOWER_LETTER.equals(randomType)) {
                sb.append(randomLowerLetter());
            } else if (RandomType.UPPER_LETTER.equals(randomType)) {
                sb.append(randomUpperLetter());
            } else if (RandomType.LETTER.equals(randomType)) {
                sb.append(randomLetter());
            } else if (RandomType.NUM_OR_LETTER.equals(randomType)) {
                sb.append(randomNumberAndLetter());
            }
        }
        return prefix + sb.toString() + suffix;
    }

    /**
     * 拼接错误的字符串
     * @param strValue 指定字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @param length 长度
     * @param randomType 随机类型
     * @return 错误的字符串
     */
    private static String spliceErrorString(String strValue, String prefix, String suffix, int length, RandomType randomType) {
        if (!"".equals(strValue)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < prefix.length(); i++) {
            result.append(randomCharacter());
        }
        for (int i = 0; i < suffix.length(); i++) {
            result.append(randomCharacter());
        }
        int randomLen = random.nextInt(10);
        int fixedLen = length == -1 ? 10 : length;
        for (int i = 0; i < randomLen + fixedLen; i++) {
            result.append(randomCharacter());
        }
        return result.toString();
    }

    /**
     * 随机产生一个字符(含数字、英文、特殊字符)
     *
     * @return 字符
     */
    private static char randomCharacter() {
        return (char) random.nextInt(256);
    }

    /**
     * 随机产生一个字符，只含数字字符
     *
     * @return 数字字符
     */
    private static char randomNumber() {
        return (char) (random.nextInt(10) + NUMBER_ASCII);
    }

    /**
     * 随机产生一个字符，只含英文字母（含大小写）
     *
     * @return 英文字母
     */
    private static char randomLetter() {
        return (char) (random.nextInt(26) + random.nextInt(2) == 0 ? UPPER_LETTER_ASCII : LOWER_LETTER_ASCII);
    }

    /**
     * 随机产生一个字符，只含小写英文字母
     *
     * @return 小写英文字母
     */
    private static char randomLowerLetter() {
        return (char) (random.nextInt(26) + LOWER_LETTER_ASCII);
    }

    /**
     * 随机产生一个字符，只含大写英文字母
     *
     * @return 大写英文字母
     */
    private static char randomUpperLetter() {
        return (char) (random.nextInt(26) + UPPER_LETTER_ASCII);
    }

    /**
     * 随机产生一个字符，只含数字和英文字母（含大小写）
     *
     * @return 数字或英文字母
     */
    private static char randomNumberAndLetter() {
        return (char) (random.nextInt(2) == 0 ?
                (random.nextInt(10) + NUMBER_ASCII) :
                (random.nextInt(26) + random.nextInt(2) == 0 ? UPPER_LETTER_ASCII : LOWER_LETTER_ASCII));
    }
}
