package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.db.annotations.ResultField;
import db.sql.api.impl.tookit.SqlUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;


public class ResultFieldInfo {

    private final boolean resultMapping;

    /**
     * 字段
     */
    private final Field field;

    /**
     * 隐射的列名
     */
    private final String mappingColumnName;


    /**
     * typeHandler
     */
    private final Class<? extends TypeHandler<?>> typeHandler;


    /**
     * jdbcType
     */
    private final JdbcType jdbcType;

    public ResultFieldInfo(Field field, ResultField resultField) {
        this(true, field, getColumnName(field, resultField), getTypeHandler(field, resultField), resultField.jdbcType());
    }

    public ResultFieldInfo(boolean resultMapping, Field field, String mappingColumnName, Class<? extends TypeHandler<?>> typeHandler, JdbcType jdbcType) {
        this.resultMapping = resultMapping;
        this.field = field;
        this.mappingColumnName = mappingColumnName;
        this.typeHandler = typeHandler;
        this.jdbcType = jdbcType;
    }

    static Class<? extends TypeHandler<?>> getTypeHandler(Field field, ResultField resultField) {
        if (field.isAnnotationPresent(cn.mybatis.mp.db.annotations.TypeHandler.class)) {
            cn.mybatis.mp.db.annotations.TypeHandler th = field.getAnnotation(cn.mybatis.mp.db.annotations.TypeHandler.class);
            return th.value();
        }
        return resultField.typeHandler();
    }

    static String getColumnName(Field field, ResultField resultField) {
        String name = resultField.value();
        if (name.isEmpty()) {
            name = SqlUtil.getAsName(field);
        }
        return name;
    }

    public boolean isResultMapping() {
        return resultMapping;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public Field getField() {
        return field;
    }

    public String getMappingColumnName() {
        return mappingColumnName;
    }

    public Class<? extends TypeHandler<?>> getTypeHandler() {
        return typeHandler;
    }
}
