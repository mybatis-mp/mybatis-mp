package cn.mybatis.mp.core.mybatis.typeHandler;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class MybatisTypeHandlerUtil {

    private final static Map<Class<? extends TypeHandler<?>>, Map<Type, Map<Class<?>, TypeHandler<?>>>> GENERIC_TYPE_HANDLERS = new ConcurrentHashMap();

    public static TypeHandler<?> createTypeHandler(Field field, Class<? extends TypeHandler<?>> typeHandlerClass) {
        if (typeHandlerClass == UnknownTypeHandler.class) {
            return null;
        }
        ReflectiveOperationException exception;
        try {
            Constructor constructor = typeHandlerClass.getConstructor(Class.class, Type.class);
            return (TypeHandler<?>) constructor.newInstance(field.getType(), field.getGenericType());
        } catch (ReflectiveOperationException e) {
            exception = e;
        }

        if (Objects.nonNull(exception)) {
            try {
                Constructor constructor = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler<?>) constructor.newInstance(field.getType());
            } catch (ReflectiveOperationException e) {
                exception = e;
            }
        }

        if (Objects.nonNull(exception)) {
            try {
                Constructor constructor = typeHandlerClass.getConstructor();
                return (TypeHandler<?>) constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                exception = e;
            }
        }

        throw new RuntimeException(exception);
    }


    public static TypeHandler<?> getTypeHandler(Configuration cfg, Class<?> type, Class<? extends TypeHandler<?>> typeHandlerClass) {
        TypeHandler<?> typeHandler = cfg.getTypeHandlerRegistry().getMappingTypeHandler(typeHandlerClass);
        if (Objects.nonNull(typeHandler)) {
            return typeHandler;
        }
        typeHandler = cfg.getTypeHandlerRegistry().getInstance(type, typeHandlerClass);
        return typeHandler;
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

    private static TypeHandler<?> getGenericTypeHandler(Class<? extends TypeHandler<?>> typeHandlerClass, Class<?> type, Type genericType) {
        return GENERIC_TYPE_HANDLERS
                .computeIfAbsent(typeHandlerClass, key -> new ConcurrentHashMap<>())
                .computeIfAbsent(genericType, key -> new ConcurrentHashMap<>())
                .computeIfAbsent(type, key -> newGenericTypeHandler(typeHandlerClass, type, genericType))
                ;
    }

    private static TypeHandler<?> newGenericTypeHandler(Class<? extends TypeHandler<?>> typeHandlerClass, Class<?> type, Type genericType) {
        try {
            Constructor constructor = typeHandlerClass.getConstructor(Class.class, Type.class);
            return (TypeHandler<?>) constructor.newInstance(type, genericType);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    public static TypeHandler<?> getTypeHandler(Configuration cfg, Field field, Class<? extends TypeHandler<?>> typeHandlerClass, JdbcType jdbcType) {
        //假如是泛型TypeHandler
        if (GenericTypeHandler.class.isAssignableFrom(typeHandlerClass)) {
            return getGenericTypeHandler(typeHandlerClass, field.getType(), field.getGenericType());
        }
        //不再封装处理 走 mybatis 自带的
        return getTypeHandler(cfg, field.getType(), typeHandlerClass, jdbcType);
    }
}
