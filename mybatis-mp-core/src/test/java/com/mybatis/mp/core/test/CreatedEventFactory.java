package com.mybatis.mp.core.test;

import com.mybatis.mp.core.test.vo.CreatedEventFetchTestVo;
import com.mybatis.mp.core.test.vo.CreatedEventNestedTestVo;
import com.mybatis.mp.core.test.vo.CreatedEventTestVo;

import java.util.Map;

public class CreatedEventFactory {

    public static void onCreatedEvent(Map<String, Object> cache, CreatedEventTestVo onCreatedEventTestVo) {
        System.out.println(cache + ">>>>>>>>>>>>>>>onCreatedEvent1.");
        cache.put("onCreatedEvent1", "11");
        onCreatedEventTestVo.setSourcePut("CreatedEventTestVo");
    }


    public static void onCreatedEvent(Map<String, Object> cache, CreatedEventNestedTestVo onCreatedEventNestedTestVo) {
        System.out.println(cache + ">>>>>>>>>>>>>>>onCreatedEvent22.");
        cache.put("onCreatedEvent2", "22");
        onCreatedEventNestedTestVo.setSourcePut("CreatedEventNestedTestVo");
    }

    public static void onCreatedEvent(CreatedEventFetchTestVo onCreatedEventFetchTestVo) {
        onCreatedEventFetchTestVo.setSourcePut("CreatedEventFetchTestVo");
    }
}
