package com.dotwait.enums;

public enum RandomType {
    ALL(0, "数字、字母以及特殊字符"),
    NUMBER(1, "数字"),
    LETTER(2, "字母"),
    LOWER_LETTER(3, "小写字母"),
    UPPER_LETTER(4, "大写字母"),
    NUM_OR_LETTER(5, "数字或字母");

    int code;
    String description;

    RandomType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
