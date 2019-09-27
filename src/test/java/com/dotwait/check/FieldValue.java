package com.dotwait.check;

import java.util.List;

/**
 * 存储各个字段生成的正确的值和错误的值
 */
public class FieldValue {
    private Object rightValue;
    private Object errorValue;

    public FieldValue(Object rightValue, Object errorValue) {
        this.rightValue = rightValue;
        this.errorValue = errorValue;
    }

    public Object getRightValue() {
        return rightValue;
    }

    public void setRightValue(Object rightValue) {
        this.rightValue = rightValue;
    }

    public Object getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(Object errorValue) {
        this.errorValue = errorValue;
    }
}
