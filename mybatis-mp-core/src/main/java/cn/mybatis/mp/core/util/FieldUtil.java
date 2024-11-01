package cn.mybatis.mp.core.util;

import cn.mybatis.mp.db.annotations.Ignore;
import org.apache.ibatis.reflection.TypeParameterResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FieldUtil {

    public static boolean isResultMappingField(Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }

        if (Modifier.isFinal(field.getModifiers())) {
            return false;
        }

        return !field.isAnnotationPresent(Ignore.class);
    }

    public static List<Field> getResultMappingFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        Set<String> fieldNameSet = new HashSet<>();
        Class parseClass = clazz;
        while (parseClass != null) {
            Field[] fields = parseClass.getDeclaredFields();
            for (Field field : fields) {
                if (isResultMappingField(field)) {
                    if (fieldNameSet.contains(field.getName())) {
                        continue;
                    }
                    fieldNameSet.add(field.getName());
                    fieldList.add(field);
                }
            }
            parseClass = parseClass.getSuperclass();
        }
        return fieldList;
    }

    public static Class<?> getFieldType(Class clazz, Field field) {
        Type type = TypeParameterResolver.resolveFieldType(field, clazz);
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getRawType();
        }
        return (Class<?>) type;
    }


    /**
     * 获取字段的真正Type,假如是List<T> id，会返回 里面的 T
     *
     * @param clazz
     * @param field
     * @return
     */
    public static Class<?> getFieldMappingType(Class clazz, Field field) {
        Type type = TypeParameterResolver.resolveFieldType(field, clazz);
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        return (Class<?>) type;
    }

}
