package com.mybatis.mp.core.test;

import com.mybatis.mp.core.test.vo.PutValueEnum;

import java.util.Arrays;
import java.util.Optional;

public class GetPutValueFactory {

    public static String getPutValue1(Integer code) {
        Optional<PutValueEnum> e = Arrays.stream(PutValueEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst();
        return e.map(PutValueEnum::getName).orElse(null);
    }
}
