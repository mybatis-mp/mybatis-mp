package db.sql.api.cmd.executor;

import db.sql.api.DbType;

import java.util.function.Consumer;

public interface DBRunnable<T, E> {

    T onDB(DbType dbType, Consumer<E> consumer);

    T elseDB(Consumer<E> consumer);

    void runOnDB(DbType dbType, E e);
}
