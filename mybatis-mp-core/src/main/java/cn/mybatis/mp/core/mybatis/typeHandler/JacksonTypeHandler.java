package cn.mybatis.mp.core.mybatis.typeHandler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Type;
import java.util.Objects;

public class JacksonTypeHandler extends AbstractJsonTypeHandler {

    private static volatile ObjectMapper OBJECT_MAPPER;

    public JacksonTypeHandler(Class<?> type, Type genericType) {
        super(type, genericType);
    }

    private ObjectMapper getObjectMapper() {
        if (Objects.isNull(OBJECT_MAPPER)) {
            OBJECT_MAPPER = new ObjectMapper();
        }
        return OBJECT_MAPPER;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }

    @Override
    String toJson(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    Object parseJson(String json) {
        ObjectMapper objectMapper = getObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(this.getDeserializeType());
        try {
            return objectMapper.readValue(json, javaType);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

}
