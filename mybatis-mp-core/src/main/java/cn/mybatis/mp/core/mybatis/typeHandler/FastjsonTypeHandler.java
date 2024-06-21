package cn.mybatis.mp.core.mybatis.typeHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;

public class FastjsonTypeHandler extends AbstractJsonTypeHandler {

    public FastjsonTypeHandler(Class<?> type, Type genericType) {
        super(type, genericType);
    }

    @Override
    String toJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
    }

    @Override
    Object parseJson(String json) {
        return JSON.parseObject(json, this.getDeserializeType());
    }
}
