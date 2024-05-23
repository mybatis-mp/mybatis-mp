package db.sql.api.impl.tookit;

import java.util.List;

public class Objects {

    public static final boolean nonNull(Object o) {
        return java.util.Objects.nonNull(o);
    }

    public static final void requireNonEmpty(String s) {
        java.util.Objects.requireNonNull(s);
        if ("".equals(s)) {
            throw new RuntimeException("can't be blank");
        }
    }

    public static final void requireNonNull(Object value) {
        java.util.Objects.requireNonNull(value);
    }

    public static final <T> void requireNonNull(T[] values) {
        java.util.Objects.requireNonNull(values);
        for (T s : values) {
            java.util.Objects.requireNonNull(s);
        }
    }

    public static final <T> void requireNonNull(List<T> values) {
        java.util.Objects.requireNonNull(values);
        for (T s : values) {
            java.util.Objects.requireNonNull(s);
        }
    }
}
