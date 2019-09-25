package com.dotwait.check;

import cn.hutool.core.bean.BeanUtil;
import com.dotwait.annotation.Limit;
import com.dotwait.constitute.Constitute;
import com.dotwait.enums.RandomType;

import java.lang.reflect.*;
import java.util.*;

import static com.dotwait.constant.CheckConstant.*;

/**
 * generate different parameters
 */
public class CheckUtil {
    private static final Random random = new Random();

    private static Stack<Integer> stack = new Stack<>();

    private static Map<String, Object> rightMap = new HashMap<>();
    /**
     * 根据不同的组合生成不同字段为null的对象
     * @param cls class对象
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
        List<Integer> numbers = constitute.generateNumbers(fields.size());
        List<List<Integer>> combinations = constitute.allCombination(numbers);
        for (List<Integer> combination : combinations) {
            List<Field> fieldsMapToCombination = getFieldsMapToCombination(fields, combination);
            Object newObj = generateNullFieldObject(cls, oldObj, fieldsMapToCombination);
            result.add(newObj);
        }
        return result;
    }

    /**
     * 根据组合获取对应的Field
     * @param fields 字段集合
     * @param combination 组合
     * @return 对应的字段
     */
    private static List<Field> getFieldsMapToCombination(List<Field> fields, List<Integer> combination){
        List<Field> result = new ArrayList<>();
        combination.forEach(index -> result.add(fields.get(index)));
        return result;
    }

    /**
     * 获取有Limit注解的字段
     * @param cls class对象
     * @return 有Limit注解的字段数组
     */
    private static List<Field> getLimitAnnotationFields(Class cls){
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field field : declaredFields) {
            Limit limit = field.getAnnotation(Limit.class);
            if (limit == null){
                continue;
            }
            fields.add(field);
        }
        return fields;
    }

    /**
     * 生成其中一个字段为null的对象集合，每个对象只有一个字段为null
     * @param cls class对象
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
            if (limit == null){
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
     * @param cls class对象
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
            if (limit == null){
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
     * 根据字段获取该字段的get方法名
     * @param field 字段
     * @return get方法名
     */
    private static String spliceSetMethodName(Field field){
        if (field == null){
            return "";
        }
        String name = field.getName();
        String firstLetter = name.substring(0, 1);
        return "set" + firstLetter.toUpperCase() + name.substring(1);
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
                field.set(obj, value);
                rightMap.put(spliceStack(), value);
                return;
            }
            String simpleName = type.getSimpleName();
            /*list*/
            if ("List".equals(simpleName)) {
                List list = dealListType(type, field, objects, limit);
                field.set(obj, list);
                rightMap.put(spliceStack(), list);
                return;
            }
            /*array*/
            if (simpleName.contains("[]")) {
                Object array = dealArrayType(type, simpleName, objects, limit);
                field.set(obj, array);
                rightMap.put(spliceStack(), array);
            }
        }
    }

    /**
     * 拼接对象字段位置
     * @return 包含字段位置的字符串
     */
    private static String spliceStack(){
        List<Integer> list = new ArrayList<>(stack);
        StringBuilder result = new StringBuilder();
        if (list.size()>0){
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
        Object array = Array.newInstance(aClass, size);
        /*是否为基础类型*/
        if (classMap.get(removeArray(simpleName)) != null) {
            List<String> methodNames = methodNameMap.get(aClass);
            if (methodNames != null && methodNames.size() > 0) {
                List<Object> childObjects = getLimitAnnotationParams(methodNames, limit);
                childObjects.forEach(System.out::println);
                for (int i = 0; i < size; i++) {
                    Array.set(array, i, baseType(removeArray(simpleName), childObjects));
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                Array.set(array, i, parseLimit(aClass));
            }
        }
        return array;
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
     *
     * @param typeSimpleName 字段类型简单名称
     * @param objects        参数值
     * @return 随机值
     */
    private static Object baseType(String typeSimpleName, List<Object> objects) {
        /*String*/
        if ("String".equals(typeSimpleName)) {
            return spliceString(String.valueOf(objects.get(0)),
                    String.valueOf(objects.get(1)), String.valueOf(objects.get(2)), (int) objects.get(3), (RandomType) objects.get(4));
        }
        /*base data*/
        if ("int".equals(typeSimpleName) || "Integer".equals(typeSimpleName)) {
            return generateRandomInt((int) objects.get(0), (int) objects.get(1), (int) objects.get(2));
        }
        if ("long".equals(typeSimpleName) || "Long".equals(typeSimpleName)) {
            return generateRandomLong((long) objects.get(0), (long) objects.get(1), (long) objects.get(2));
        }
        if ("float".equals(typeSimpleName) || "Float".equals(typeSimpleName)) {
            return generateRandomFloat((float) objects.get(0), (float) objects.get(1), (float) objects.get(2));
        }
        if ("double".equals(typeSimpleName) || "Double".equals(typeSimpleName)) {
            return generateRandomDouble((double) objects.get(0), (double) objects.get(1), (double) objects.get(2));
        }
        if ("char".equals(typeSimpleName) || "Character".equals(typeSimpleName)) {
            return generateRandomChar((char) objects.get(0));
        }
        if ("boolean".equals(typeSimpleName) || "Boolean".equals(typeSimpleName)) {
            return generateRandomBoolean((boolean) objects.get(0), (boolean) objects.get(1));
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
