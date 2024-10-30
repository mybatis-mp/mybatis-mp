package com.mybatis.mp.core.test;

import com.mybatis.mp.core.test.vo.CreatedEventFetchTestVo;
import com.mybatis.mp.core.test.vo.CreatedEventNestedTestVo;
import com.mybatis.mp.core.test.vo.CreatedEventTestVo;

public class CreatedEventFactory {

    public static void createdEvent(CreatedEventTestVo createdEventTestVo) {
        createdEventTestVo.setSourcePut("CreatedEventTestVo");
    }

    public static void createdEvent(CreatedEventNestedTestVo createdEventNestedTestVo) {
        createdEventNestedTestVo.setSourcePut("CreatedEventNestedTestVo");
    }

    public static void createdEvent(CreatedEventFetchTestVo createdEventFetchTestVo) {
        createdEventFetchTestVo.setSourcePut("CreatedEventFetchTestVo");
    }
}
