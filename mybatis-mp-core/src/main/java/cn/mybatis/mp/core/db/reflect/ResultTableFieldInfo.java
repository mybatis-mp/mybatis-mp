package cn.mybatis.mp.core.db.reflect;

import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;


public class ResultTableFieldInfo extends ResultFieldInfo {

    private final TableInfo tableInfo;

    private final TableFieldInfo tableFieldInfo;

    private final int storey;

    private final Class type;

    public ResultTableFieldInfo(Class type, int storey, String tablePrefix, TableInfo tableInfo, TableFieldInfo tableFieldInfo, Field field) {
        this(true, type, storey, tablePrefix, tableInfo, tableFieldInfo, field);
    }

    public ResultTableFieldInfo(boolean resultMapping, Class type, int storey, String tablePrefix, TableInfo tableInfo, TableFieldInfo tableFieldInfo, Field field) {
        super(resultMapping, type, field, tablePrefix + tableFieldInfo.getColumnName(), getTypeHandler(field, tableFieldInfo), tableFieldInfo.getTableFieldAnnotation().jdbcType());
        this.type = type;
        this.tableInfo = tableInfo;
        this.tableFieldInfo = tableFieldInfo;
        this.storey = storey;
    }

    static Class<? extends TypeHandler<?>> getTypeHandler(Field field, TableFieldInfo tableFieldInfo) {
        if (field.isAnnotationPresent(cn.mybatis.mp.db.annotations.TypeHandler.class)) {
            cn.mybatis.mp.db.annotations.TypeHandler th = field.getAnnotation(cn.mybatis.mp.db.annotations.TypeHandler.class);
            return th.value();
        } else {
            return tableFieldInfo.getTableFieldAnnotation().typeHandler();
        }
    }

    public int getStorey() {
        return storey;
    }

    public Class getType() {
        return type;
    }

    public TableFieldInfo getTableFieldInfo() {
        return tableFieldInfo;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }
}
