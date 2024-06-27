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

    private final Fetch fetch;

    private final String valueColumn;

    private final TypeHandler<?> valueTypeHandler;

    private final String targetMatchColumn;

    private final String targetSelectColumn;

    private final String orderBy;

    private final GetFieldInvoker eqGetFieldInvoker;

    private final SetFieldInvoker writeFieldInvoker;

    private final boolean multiple;

    private final Class<?> returnType;

    public FetchInfo(Field field, Fetch fetch, Class returnType, String valueColumn, TypeHandler<?> valueTypeHandler, Field targetMatchField, String targetMatchColumn, String targetSelectColumn, String orderBy) {
        this.field = field;
        this.fetch = fetch;
        this.writeFieldInvoker = new SetFieldInvoker(field);
        this.valueColumn = valueColumn;
        this.valueTypeHandler = valueTypeHandler;
        this.eqGetFieldInvoker = Objects.isNull(targetMatchField) ? null : new GetFieldInvoker(targetMatchField);
        this.targetMatchColumn = targetMatchColumn;
        this.targetSelectColumn = targetSelectColumn;
        this.multiple = Collection.class.isAssignableFrom(field.getType());
        this.returnType = returnType;
        this.orderBy = orderBy;
    }

    public void setValue(Object object, Object value) {
        try {
            writeFieldInvoker.invoke(object, new Object[]{value});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
