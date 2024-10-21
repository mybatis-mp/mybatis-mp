package db.sql.api.impl.tookit;

import java.util.List;

public final class Lists {


    public static <T> List<T> merge(List<T> list, T[] ts) {
        if (ts == null || ts.length < 1) {
            return list;
        }
        for (T t : ts) {
            list.add(t);
        }
        return list;
    }
}
