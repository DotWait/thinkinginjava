package com.dotwait.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldLimit {
    /*
    格式为:
    1 表示限制数字只能为1
    1- 表示数字大于等于1
    -1 表示数字小于等于1
    1-4 表示数字在可能为1或2或3或4
     */
    String numberLimit() default "";
    /*
    格式为：
    abc 表示以abc开头
     */
    String stringPrefix() default "";
    /*
    abc 表示以abc结尾
     */
    String stringSuffix() default "";
    /*
    字符串长度
     */
    int stringLength() default 0;
}
