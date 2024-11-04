package com.mybatis.mp.core.test.vo;

public enum PutValueEnum {

    ENUM1(1, "测试ENUM1"),
    ENUM2(2, "测试ENUM2"),

    ;

    private final Integer code;

    private final String name;

    PutValueEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
