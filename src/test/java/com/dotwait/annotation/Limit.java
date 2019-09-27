package com.dotwait.annotation;

import com.dotwait.enums.RandomType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Limit {
    /**
     * String类型的字段值
     * @return 字段值
     */
    String strValue() default "";
    /**
     * String类型的字段前缀
     *
     * @return 字段前缀
     */
    String prefix() default "";

    /**
     * String类型的字段后缀
     * @return 字段后缀
     */
    String suffix() default "";

    /**
     * String类型的字段长度
     * @return 字段长度
     */
    int length() default -1;

    /**
     * 字符串随机类型
     * @return 随机类型
     */
    RandomType randomType() default RandomType.NUM_OR_LETTER;

    /**
     * int或Integer类型的字段值
     * @return 字段值
     */
    int intValue() default Integer.MIN_VALUE;

    /**
     * int类型的字段值上限
     * @return 字段值上限
     */
    int intUpperLimit() default Integer.MAX_VALUE;

    /**
     * int类型的字段值下限
     * @return 字段值下限
     */
    int intLowerLimit() default Integer.MIN_VALUE;

    /**
     * long类型的字段值
     * @return 字段值
     */
    long longValue() default Long.MIN_VALUE;

    /**
     * long类型的字段值上限
     * @return 字段值上限
     */
    long longUpperLimit() default Long.MAX_VALUE;

    /**
     * long类型的字段值下限
     * @return 字段值下限
     */
    long longLowerLimit() default Long.MIN_VALUE;

    /**
     * float类型的字段值
     * @return 字段值
     */
    float floatValue() default Float.MIN_VALUE;

    /**
     * float类型的字段值上限
     * @return 字段值上限
     */
    float floatUpperLimit() default Float.MAX_VALUE;

    /**
     * float类型的字段值下限
     * @return 字段值下限
     */
    float floatLowerLimit() default Float.MIN_VALUE;

    /**
     * double类型的字段值
     * @return 字段值
     */
    double doubleValue() default Double.MIN_VALUE;

    /**
     * double类型的字段值上限
     * @return 字段值上限
     */
    double doubleUpperLimit() default Double.MAX_VALUE;

    /**
     * float类型的字段值下限
     * @return 字段值下限
     */
    double doubleLowerLimit() default Double.MIN_VALUE;

    /**
     * boolean类型的字段值
     * @return 字段值
     */
    boolean booleanValue() default false;

    /**
     * boolean类型字段值是否随机产生
     * @return 随机产生的字段值
     */
    boolean booleanRandom() default false;

    /**
     * char类型的字段值
     * @return 字段值
     */
    char charValue() default 0;

    /**
     * 集合的大小
     * @return 集合大小
     */
    int size() default 1;

    /**
     * 字段是否必填
     * @return 是否必填
     */
    boolean isRequired() default true;
}
