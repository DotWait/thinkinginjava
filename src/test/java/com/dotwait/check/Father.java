package com.dotwait.check;

import com.dotwait.annotation.Limit;
import com.dotwait.enums.RandomType;

import java.util.Arrays;
import java.util.List;

public class Father {
    @Limit(prefix = "prefix", suffix = "suffix", length = 20, randomType = RandomType.NUMBER)
    private String name;
    @Limit(prefix = "prefix", suffix = "suffix", length = 20)
    private String id;
    @Limit(intValue = 12)
    private Integer age;
    @Limit(intLowerLimit = -5, intUpperLimit = 20)
    private Integer score;
    @Limit(prefix = "phone", length = 15)
    private List<String> phones;
    @Limit(prefix = "hobby", length = 10)
    private String[] hobbys;
    @Limit(intLowerLimit = 10, intUpperLimit = 100, size = 2)
    private List<Integer> numbers;
    @Limit(intLowerLimit = 100, intUpperLimit = 200)
    private Integer[] stuNums;
    @Limit()
    private List<Son> sons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String[] getHobbys() {
        return hobbys;
    }

    public void setHobbys(String[] hobbys) {
        this.hobbys = hobbys;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public Integer[] getStuNums() {
        return stuNums;
    }

    public void setStuNums(Integer[] stuNums) {
        this.stuNums = stuNums;
    }

    public List<Son> getSons() {
        return sons;
    }

    public void setSons(List<Son> sons) {
        this.sons = sons;
    }

    @Override
    public String toString() {
        return "Father{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", age=" + age +
                ", score=" + score +
                ", phones=" + phones +
                ", hobbys=" + Arrays.toString(hobbys) +
                ", numbers=" + numbers +
                ", stuNums=" + Arrays.toString(stuNums) +
                ", sons=" + sons +
                '}';
    }
}
