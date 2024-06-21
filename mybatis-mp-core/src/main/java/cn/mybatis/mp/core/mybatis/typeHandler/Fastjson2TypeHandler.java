package cn.mybatis.mp.core.mybatis.typeHandler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter.Feature;

import java.lang.reflect.Type;

public class Fastjson2TypeHandler extends AbstractJsonTypeHandler {

    public Fastjson2TypeHandler(Class<?> type, Type genericType) {
        super(type, genericType);
    }

    @Override
    String toJson(Object obj) {
        return JSON.toJSONString(obj, Feature.WriteMapNullValue, Feature.WriteNullListAsEmpty, Feature.WriteNullStringAsEmpty);
    }

    @Override
    Object parseJson(String json) {
        return JSON.parseObject(json, this.getDeserializeType());
    }
}
