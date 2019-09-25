package com.dotwait.check;

import com.dotwait.annotation.Limit;

import java.util.List;

public class Son {
    @Limit(length = 10)
    private String id;
    @Limit(prefix = "name", length = 10)
    private String name;
    @Limit(intValue = 99)
    private Integer age;
    @Limit(intValue = 34)
    private Integer stuNum;
    @Limit(prefix = "ph", length = 10, size = 2)
    private List<String> phones;

    @Override
    public String toString() {
        return "Son{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", stuNum=" + stuNum +
                ", phones=" + phones +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getStuNum() {
        return stuNum;
    }

    public void setStuNum(Integer stuNum) {
        this.stuNum = stuNum;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
