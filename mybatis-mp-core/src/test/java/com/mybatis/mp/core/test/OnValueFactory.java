package com.mybatis.mp.core.test;

import com.mybatis.mp.core.test.vo.OnValueFetchVo;
import com.mybatis.mp.core.test.vo.OnValueNestedVo;
import com.mybatis.mp.core.test.vo.OnValueVo;

public class OnValueFactory {

    public static void onValue(OnValueVo onValueVo) {
        onValueVo.setSourcePut("OnValueVo");
    }

    public static void onValue(OnValueNestedVo onValueNestedVo) {
        onValueNestedVo.setSourcePut("OnValueNestedVo");
    }

    public static void onValue(OnValueFetchVo onValueFetchVo) {
        onValueFetchVo.setSourcePut("OnValueFetchVo");
    }
}
