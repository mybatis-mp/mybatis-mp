package db.sql.api.impl.tookit;

import java.util.Collection;
import java.util.List;

public final class Objects {

    public static boolean nonNull(Object o) {
        return java.util.Objects.nonNull(o);
    }

    public static boolean isNull(Object o) {
        return java.util.Objects.isNull(o);
    }

    public static void requireNonEmpty(Object value) {
        java.util.Objects.requireNonNull(value);
        if (value instanceof String) {
            if ("".equals(value)) {
                throw new RuntimeException("can't be blank");
            }
        } else if (value instanceof Object[]) {
            Object[] values = (Object[]) value;
            if (values.length < 1) {
                throw new RuntimeException("can't be empty");
            }
        } else if (value instanceof Collection) {
            if (((Collection) value).isEmpty()) {
                throw new RuntimeException("can't be empty");
            }
        }
    }

    public static void requireNonNull(Object value) {
        java.util.Objects.requireNonNull(value);
    }

    public static <T> void requireNonNull(T[] values) {
        java.util.Objects.requireNonNull(values);
        for (T s : values) {
            java.util.Objects.requireNonNull(s);
        }
    }

    public static <T> void requireNonNull(List<T> values) {
        java.util.Objects.requireNonNull(values);
        for (T s : values) {
            java.util.Objects.requireNonNull(s);
        }
    }
}
