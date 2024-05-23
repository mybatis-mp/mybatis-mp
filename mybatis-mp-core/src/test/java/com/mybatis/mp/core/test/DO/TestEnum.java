package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.core.mybatis.typeHandler.EnumSupport;

public enum TestEnum implements EnumSupport<String> {

    X1("a1"), X2("a2");

    private final String code;

    TestEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
