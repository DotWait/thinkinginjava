package com.dotwait.reflect;

import com.dotwait.annotation.Limit;
import com.dotwait.check.Father;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.List;

public class MyReflect {
    @Test
    public void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        /*获取Class对象*/
        Class<?> myReflectClass = Class.forName("com.dotwait.reflect.MyReflect");
        Class<Integer> integerClass = int.class;
        Class<Integer> type = Integer.TYPE;
        StringBuilder stringBuilder = new StringBuilder("hello123");
        Class<? extends StringBuilder> stringBuilderClass = stringBuilder.getClass();
        /*通过反射生成对象*/
        MyReflect myReflect = (MyReflect)myReflectClass.newInstance();
        myReflect.show();
        Constructor<?> constructor = myReflectClass.getConstructor();
        MyReflect o = (MyReflect)constructor.newInstance();
        o.show();

        /*获取方法*/
        Method[] declaredMethods = myReflectClass.getDeclaredMethods();
        for (Method method : declaredMethods){
            System.out.println(method);
        }
    }

    public MyReflect(){
        System.out.println("this is MyReflect constructor");
    }

    public void show(){

    }

    @Test
    public void limitTest(){
        Class<Limit> limitClass = Limit.class;
        Method[] methods = limitClass.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }

    @Test
    public void typeTest() throws InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        System.out.println(int.class.getSimpleName());
        show(Father.class);
    }

    public void show(Class cls) throws InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Field[] declaredFields = cls.getDeclaredFields();
        Object obj = cls.newInstance();
        for (Field field : declaredFields) {
//            Limit annotation = field.getAnnotation(Limit.class);
//            Class<? extends Limit> aClass = annotation.getClass();
//            Method[] methods = aClass.getDeclaredMethods();
//            for (Method method : methods) {
//                System.out.println("method name ==> " + method.getName());
//                if (method.getName().equals("equals") || method.getName().equals("toString") || method.getName().equals("hashCode")){
//                    continue;
//                }
//                System.out.println(method.invoke(annotation));
//            }
//            System.out.println("==============");
            Class<?> type = field.getType();

            field.setAccessible(true);
            Object a = 2;
            if (field.getType().equals(String.class)){
                field.set(obj, "1");
            }
            if (field.getType().equals(int.class)){
                field.set(obj, a);
            }
            System.out.println(field.getType().getSimpleName());
//            String name = type.getName();
//            String simpleName = type.getSimpleName();
//            System.out.println("simpleName ==> " + simpleName);
//            System.out.println("name ==> " + name);
            if (type.equals(List.class)){
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Type[] actualTypeArguments = genericType.getActualTypeArguments();
                System.out.println(actualTypeArguments.length);
                for (Type argument : actualTypeArguments) {
                    System.out.println("argument ==> " + argument);
                    Class<?> aClass = Class.forName(argument.getTypeName());
                    System.out.println("class ==> " + aClass);
                }
            }
//            System.out.println(type);
        }
        System.out.println((Father)obj);
        System.out.println("hello java reflect");
    }

    @Test
    public void arrayTest(){

    }
}
