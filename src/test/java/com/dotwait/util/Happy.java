package com.dotwait.util;

import com.dotwait.annotation.FieldLimit;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.intellij.lang.annotations.Identifier;

public class Happy {
    @FieldLimit(numberLimit = "1-100")
    private Integer age;
    @FieldLimit(stringPrefix = "aa", stringLength = 10)
    private String name;
    @FieldLimit(numberLimit = "0-")
    private Integer time;

    public Happy(Integer age, String name, Integer time) {
        this.age = age;
        this.name = name;
        this.time = time;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Happy{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
