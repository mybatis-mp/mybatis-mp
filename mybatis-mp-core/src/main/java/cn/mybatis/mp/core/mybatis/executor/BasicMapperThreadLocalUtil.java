package cn.mybatis.mp.core.mybatis.executor;

import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;

import java.util.function.Supplier;

public final class BasicMapperThreadLocalUtil {

    private final static ThreadLocal<Object> basicMapperThreadLocal = new ThreadLocal<>();

    public static void set(Object value) {
        basicMapperThreadLocal.set(value);
    }

    public static BasicMapper get() {
        Object value = basicMapperThreadLocal.get();
        if (value == null) {
            return null;
        }
        if (value instanceof BasicMapper) {
            return (BasicMapper) value;
        }
        return ((Supplier<BasicMapper>) value).get();
    }

    public static void clear() {
        basicMapperThreadLocal.remove();
    }
}
