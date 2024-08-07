package cn.mybatis.mp.core.util;

import cn.mybatis.mp.db.annotations.Ignore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
        Class parseClass = clazz;
        while (parseClass != null) {
            Field[] fields = parseClass.getDeclaredFields();
            for (Field field : fields) {
                if (isResultMappingField(field)) {
                    fieldList.add(field);
                }
            }
            parseClass = parseClass.getSuperclass();
        }
        return fieldList;
    }

}
