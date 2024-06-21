package cn.mybatis.mp.core.mybatis.typeHandler;


import org.apache.ibatis.type.BaseTypeHandler;

import java.lang.reflect.Type;


/**
 * GenericTypeHandler，泛型基础 TypeHandler
 *
 * @param <T>
 */
public abstract class GenericTypeHandler<T> extends BaseTypeHandler<T> {

    /**
     * 目标类
     */
    protected final Class<?> type;

    /**
     * 目标字段上的泛型
     */
    protected final Type genericType;

    public GenericTypeHandler(Class<?> type) {
        this(type, null);
    }

    public GenericTypeHandler(Class<?> type, Type genericType) {
        this.type = type;
        this.genericType = genericType;
    }

    public Class<?> getType() {
        return type;
    }

    public Type getGenericType() {
        return genericType;
    }
}
