package com.mybatis.mp.core.test;

import com.mybatis.mp.core.test.vo.CreatedEventFetchTestVo;
import com.mybatis.mp.core.test.vo.CreatedEventNestedTestVo;
import com.mybatis.mp.core.test.vo.CreatedEventTestVo;

public class CreatedEventFactory {

    public static void onCreatedEvent(CreatedEventTestVo onCreatedEventTestVo) {
        onCreatedEventTestVo.setSourcePut("CreatedEventTestVo");
    }

    public static void onCreatedEvent(CreatedEventNestedTestVo onCreatedEventNestedTestVo) {
        onCreatedEventNestedTestVo.setSourcePut("CreatedEventNestedTestVo");
    }

    public static void onCreatedEvent(CreatedEventFetchTestVo onCreatedEventFetchTestVo) {
        onCreatedEventFetchTestVo.setSourcePut("CreatedEventFetchTestVo");
    }
}
