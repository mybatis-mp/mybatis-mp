package cn.mybatis.mp.core.mybatis.typeHandler;

import cn.mybatis.mp.core.db.reflect.FieldInfo;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class MybatisTypeHandlerUtil {

    private final static Map<Class<? extends TypeHandler<?>>, Map<Type, Map<Class<?>, TypeHandler<?>>>> GENERIC_TYPE_HANDLERS = new ConcurrentHashMap();

    public static TypeHandler<?> createTypeHandler(FieldInfo fieldInfo, Class<? extends TypeHandler<?>> typeHandlerClass) {
        if (typeHandlerClass == UnknownTypeHandler.class) {
            return null;
        }
        ReflectiveOperationException exception;
        try {
            Constructor constructor = typeHandlerClass.getConstructor(Class.class, Type.class);
            return (TypeHandler<?>) constructor.newInstance(fieldInfo.getTypeClass(), fieldInfo.getField().getGenericType());
        } catch (ReflectiveOperationException e) {
            //exception = e;
        }


        try {
            Constructor constructor = typeHandlerClass.getConstructor(Class.class);
            return (TypeHandler<?>) constructor.newInstance(fieldInfo.getTypeClass());
        } catch (ReflectiveOperationException e) {
            //exception = e;
        }

        try {
            Constructor constructor = typeHandlerClass.getConstructor();
            return (TypeHandler<?>) constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            exception = e;
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

    public static TypeHandler<?> getTypeHandler(Configuration cfg, FieldInfo fieldInfo, Class<? extends TypeHandler<?>> typeHandlerClass, JdbcType jdbcType) {
        //假如是泛型TypeHandler
        if (GenericTypeHandler.class.isAssignableFrom(typeHandlerClass)) {
            return getGenericTypeHandler(typeHandlerClass, fieldInfo.getTypeClass(), fieldInfo.getField().getGenericType());
        }

        if (typeHandlerClass == UnknownTypeHandler.class) {
            TypeHandler typeHandler = cfg.getTypeHandlerRegistry().getTypeHandler(fieldInfo.getTypeClass(), jdbcType);
            if (Objects.nonNull(typeHandler)) {
                return typeHandler;
            }
        }
        TypeHandler<?> typeHandler = cfg.getTypeHandlerRegistry().getMappingTypeHandler(typeHandlerClass);
        if (Objects.nonNull(typeHandler)) {
            return typeHandler;
        }
        typeHandler = cfg.getTypeHandlerRegistry().getInstance(fieldInfo.getTypeClass(), typeHandlerClass);
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


}
