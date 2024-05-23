package cn.mybatis.mp.core.util;

import java.math.BigInteger;
import java.util.Objects;

public class IdValueConverter {

    public static Object convert(Object id, Class<?> targetType) {
        if (Objects.isNull(id)) {
            return id;
        }
        if (targetType.isAssignableFrom(id.getClass())) {
            return id;
        }
        if (targetType == String.class) {
            return id.toString();
        }
        if (id instanceof Number) {
            Number idN = (Number) id;
            if (targetType == Byte.class || targetType == byte.class) {
                return idN.byteValue();
            }
            if (targetType == Integer.class || targetType == int.class) {
                return idN.intValue();
            }
            if (targetType == Long.class || targetType == long.class) {
                return idN.longValue();
            }
            if (targetType == BigInteger.class) {
                return new BigInteger(idN.toString());
            }
        }

        return id;
    }
}
