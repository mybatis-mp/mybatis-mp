package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.mybatis.typeHandler.MybatisTypeHandlerUtil;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.core.util.TypeConvertUtil;
import cn.mybatis.mp.db.annotations.*;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

public class TableFieldInfo {

    /**
     * 字段
     */
    private final Field field;

    /**
     * 字段类型信息
     */
    private final FieldInfo fieldInfo;

    /**
     * 列名
     */
    private final String columnName;


    /**
     * 字段读取反射方法
     */
    private final GetFieldInvoker readFieldInvoker;

    /**
     * TableField 注解信息
     */
    private final TableField tableFieldAnnotation;

    private final boolean tableId;

    private final boolean version;

    private final boolean tenantId;

    private final boolean logicDelete;

    /**
     * 逻辑删除注解
     */
    private final LogicDelete logicDeleteAnnotation;

    private final Object logicDeleteInitValue;

    private final SetFieldInvoker writeFieldInvoker;

    private final TypeHandler<?> typeHandler;

    public TableFieldInfo(Class clazz, Field field) {
        this.field = field;
        this.fieldInfo = new FieldInfo(clazz, field);
        this.tableFieldAnnotation = TableInfoUtil.getTableFieldAnnotation(field);
        this.columnName = TableInfoUtil.getFieldColumnName(field);
        this.readFieldInvoker = new GetFieldInvoker(field);
        this.tableId = field.isAnnotationPresent(TableId.class) || field.isAnnotationPresent(TableId.List.class);
        this.version = field.isAnnotationPresent(Version.class);
        this.tenantId = field.isAnnotationPresent(TenantId.class);
        this.logicDelete = field.isAnnotationPresent(LogicDelete.class);
        this.logicDeleteAnnotation = this.logicDelete ? field.getAnnotation(LogicDelete.class) : null;
        this.logicDeleteInitValue = this.logicDelete ? TypeConvertUtil.convert(this.logicDeleteAnnotation.beforeValue(), fieldInfo.getTypeClass()) : null;
        if (this.logicDelete) {
            LogicDeleteUtil.getLogicAfterValue(this);
        }
        this.writeFieldInvoker = new SetFieldInvoker(field);
        typeHandler = MybatisTypeHandlerUtil.createTypeHandler(this.fieldInfo, this.tableFieldAnnotation.typeHandler());
    }

    public Object getValue(Object object) {
        try {
            return this.readFieldInvoker.invoke(object, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Field getField() {
        return field;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public GetFieldInvoker getReadFieldInvoker() {
        return readFieldInvoker;
    }

    public TableField getTableFieldAnnotation() {
        return tableFieldAnnotation;
    }

    public boolean isTableId() {
        return tableId;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isTenantId() {
        return tenantId;
    }

    public boolean isLogicDelete() {
        return logicDelete;
    }

    public LogicDelete getLogicDeleteAnnotation() {
        return logicDeleteAnnotation;
    }

    public Object getLogicDeleteInitValue() {
        return logicDeleteInitValue;
    }

    public SetFieldInvoker getWriteFieldInvoker() {
        return writeFieldInvoker;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }
}
