package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.mybatis.typeHandler.GenericTypeHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

public final class MybatisTypeHandlerUtil {

    public static TypeHandler<?> getTypeHandler(Configuration cfg, Class<?> type, Class<? extends TypeHandler<?>> typeHandlerClass) {
        TypeHandler<?> typeHandler = cfg.getTypeHandlerRegistry().getMappingTypeHandler(typeHandlerClass);
        if (Objects.nonNull(typeHandler)) {
            return typeHandler;
        }
        return cfg.getTypeHandlerRegistry().getInstance(type, typeHandlerClass);
    }

    private static TypeHandler<?> getTypeHandler(Configuration cfg, Class<?> type, Class<? extends TypeHandler<?>> typeHandlerClass, JdbcType jdbcType) {
        if (typeHandlerClass == UnknownTypeHandler.class) {
            TypeHandler typeHandler = cfg.getTypeHandlerRegistry().getTypeHandler(type, jdbcType);
            if (Objects.nonNull(typeHandler)) {
                return typeHandler;
            }
        }
        TypeHandler<?> typeHandler = cfg.getTypeHandlerRegistry().getMappingTypeHandler(typeHandlerClass);
        if (Objects.nonNull(typeHandler)) {
            return typeHandler;
        }
        typeHandler = cfg.getTypeHandlerRegistry().getInstance(type, typeHandlerClass);
        return typeHandler;
    }


    public static TypeHandler<?> getTypeHandler(Configuration cfg, Field field, Class<? extends TypeHandler<?>> typeHandlerClass, JdbcType jdbcType) {
        //假如是泛型TypeHandler
        if (GenericTypeHandler.class.isAssignableFrom(typeHandlerClass)) {
            try {
                Constructor constructor = typeHandlerClass.getConstructor(Class.class, Type.class);
                return (TypeHandler<?>) constructor.newInstance(field.getType(), field.getGenericType());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        //不再封装处理 走 mybatis 自带的
        return getTypeHandler(cfg, field.getType(), typeHandlerClass, jdbcType);
    }
}
