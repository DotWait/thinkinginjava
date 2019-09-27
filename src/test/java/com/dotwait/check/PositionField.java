package com.dotwait.check;

import java.lang.reflect.Field;
import java.util.List;

public class PositionField {
    private Class cls;
    private Object obj;
    private List<Field> fields;

    public PositionField() {
    }

    public PositionField(Class cls, Object obj, List<Field> fields) {
        this.cls = cls;
        this.obj = obj;
        this.fields = fields;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "PositionField{" +
                "cls=" + cls +
                ", obj=" + obj +
                ", fields=" + fields +
                '}';
    }
}
