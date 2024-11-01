package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.util.FieldUtil;
import lombok.Data;

import java.lang.reflect.Field;


@Data
public class FieldInfo {

    private Class<?> clazz;

    /**
     * 字段
     */
    private Field field;

    /**
     * 字段的类型
     */
    private Class<?> typeClass;

    /**
     * 字段的映射类型 假如是List<T>,则是T的具体类型
     */
    private Class<?> finalClass;

    /**
     * @param clazz 具体类的class，假如是继承，不能父类
     * @param field
     */
    public FieldInfo(Class clazz, Field field) {
        this.clazz = clazz;
        this.field = field;
        this.typeClass = FieldUtil.getFieldType(clazz, field);
        this.finalClass = FieldUtil.getFieldFinalType(clazz, field);
    }
}
