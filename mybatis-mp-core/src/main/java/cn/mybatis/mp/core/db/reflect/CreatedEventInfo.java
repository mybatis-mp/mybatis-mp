package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.db.annotations.CreatedEvent;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

@Data
public class CreatedEventInfo {

    private final Class clazz;

    private final CreatedEvent annotation;

    private final Method method;

    private final boolean hasContextParam;

    public CreatedEventInfo(Class clazz, CreatedEvent annotation) {
        this.clazz = clazz;
        this.annotation = annotation;
        Method method;
        boolean hasContextParam;
        try {
            method = annotation.value().getDeclaredMethod("onCreatedEvent", Map.class, clazz);
            hasContextParam = true;
        } catch (NoSuchMethodException e) {
            try {
                method = annotation.value().getDeclaredMethod("onCreatedEvent", clazz);
                hasContextParam = false;
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
        this.method = method;
        this.hasContextParam = hasContextParam;
    }
}
