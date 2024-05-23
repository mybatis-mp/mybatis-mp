package db.sql.api.cmd.executor.method;

import db.sql.api.Cmd;
import db.sql.api.Getter;

import java.util.function.Function;

public interface IUpdateMethod<SELF extends IUpdateMethod, TABLE, TABLE_FILED, COLUMN, V> {

    SELF update(TABLE... tables);

    SELF update(Class... entities);

    SELF set(COLUMN field, V value);

    <T> SELF set(Getter<T> field, V value);

    <T> SELF set(Getter<T> field, Function<TABLE_FILED, Cmd> function);

    /**
     * 实体类修改拦截
     *
     * @param entity
     */
    default void updateEntityIntercept(Class entity) {

    }
}
