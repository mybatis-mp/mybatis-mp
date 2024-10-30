package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.util.TypeConvertUtil;
import cn.mybatis.mp.db.annotations.PutEnumValue;
import lombok.Getter;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

@Getter
public class PutEnumValueInfo {

    private final Field field;

    private final String valueColumn;

    private final TypeHandler<?> valueTypeHandler;

    private final SetFieldInvoker writeFieldInvoker;

    private final PutEnumValue annotation;

    private final Object defaultValue;

    public PutEnumValueInfo(Field field, String valueColumn, TypeHandler<?> valueTypeHandler, PutEnumValue annotation) {
        this.field = field;
        this.valueColumn = valueColumn;
        this.valueTypeHandler = valueTypeHandler;
        this.writeFieldInvoker = new SetFieldInvoker(field);
        this.annotation = annotation;
        if (!annotation.defaultValue().isEmpty()) {
            this.defaultValue = TypeConvertUtil.convert(annotation.defaultValue(), field.getType());
        } else {
            this.defaultValue = null;
        }
    }
}
