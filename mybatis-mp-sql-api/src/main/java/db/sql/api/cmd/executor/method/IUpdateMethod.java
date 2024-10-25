package db.sql.api.cmd.executor.method;

import db.sql.api.Cmd;
import db.sql.api.Getter;

import java.util.function.Function;

public interface IUpdateMethod<SELF extends IUpdateMethod, TABLE, TABLE_FILED, V> {

    SELF update(TABLE... tables);

    SELF update(Class... entities);

    SELF set(TABLE_FILED field, V value);

    <T> SELF set(Getter<T> field, V value);

    <T, T2> SELF set(Getter<T> target, Getter<T2> source);

    default <T, T2> SELF set(boolean when, Getter<T> target, Getter<T2> source) {
        if (!when) {
            return (SELF) this;
        }
        return set(target, source);
    }

    <T> SELF set(Getter<T> field, Function<TABLE_FILED, Cmd> f);

    /**
     * 实体类修改拦截
     *
     * @param entity
     */
    default void updateEntityIntercept(Class entity) {

    }
}
