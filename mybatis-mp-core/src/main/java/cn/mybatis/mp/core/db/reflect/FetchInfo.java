package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.db.annotations.Fetch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

@Data
@EqualsAndHashCode
public class FetchInfo {

    private final Field field;

    private final FieldInfo fieldInfo;

    private final Fetch fetch;

    private final String valueColumn;

    private final TypeHandler<?> valueTypeHandler;

    private final String targetMatchColumn;

    private final String targetSelectColumn;

    private final String orderBy;

    private final String groupBy;

    private final GetFieldInvoker eqGetFieldInvoker;

    private final SetFieldInvoker writeFieldInvoker;

    private final boolean multiple;

    private final Class<?> returnType;

    private final boolean isUseIn;

    private final boolean isUseResultFetchKeyValue;

    public FetchInfo(Class clazz, Field field, Fetch fetch, Class returnType, String valueColumn, TypeHandler<?> valueTypeHandler, Field targetMatchField, String targetMatchColumn, String targetSelectColumn, String orderBy, String groupBy) {
        this.field = field;
        this.fieldInfo = new FieldInfo(clazz, field);
        this.fetch = fetch;
        this.writeFieldInvoker = new SetFieldInvoker(field);
        this.valueColumn = valueColumn;
        this.valueTypeHandler = valueTypeHandler;
        this.eqGetFieldInvoker = Objects.isNull(targetMatchField) ? null : new GetFieldInvoker(targetMatchField);
        this.targetMatchColumn = targetMatchColumn;
        this.targetSelectColumn = targetSelectColumn;
        this.multiple = Collection.class.isAssignableFrom(this.fieldInfo.getTypeClass());
        this.returnType = returnType;
        this.orderBy = orderBy;
        this.groupBy = groupBy;

        boolean isUseIn = true;

        if (fetch.limit() >= 1) {
            isUseIn = false;
        } else if (!fetch.forceUseIn() && Objects.isNull(this.eqGetFieldInvoker) && this.targetSelectColumn.contains("(")) {
            isUseIn = false;
        }

        this.isUseIn = isUseIn;
        this.isUseResultFetchKeyValue = this.isUseIn && Objects.isNull(this.eqGetFieldInvoker) && this.returnType.getPackage().getName().contains("java.lang");
    }

    public void setValue(Object object, Object value) {
        try {
            writeFieldInvoker.invoke(object, new Object[]{value});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
