package com.dotwait.check;

import java.util.List;

/**
 * 存储各个字段生成的正确的值和错误的值
 */
public class FieldValue {
    /**
     * 正确的值
     */
    private List<Object> rightValues;
    /**
     * 错误的值
     */
    private List<Object> errorvalues;

    public List<Object> getRightValues() {
        return rightValues;
    }

    public void setRightValues(List<Object> rightValues) {
        this.rightValues = rightValues;
    }

    public List<Object> getErrorvalues() {
        return errorvalues;
    }

    public void setErrorvalues(List<Object> errorvalues) {
        this.errorvalues = errorvalues;
    }

    @Override
    public String toString() {
        return "FieldValue{" +
                "rightValues=" + rightValues +
                ", errorvalues=" + errorvalues +
                '}';
    }
}
