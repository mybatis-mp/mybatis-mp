package db.sql.api.cmd.executor;

import db.sql.api.DbType;

import java.util.function.Consumer;

public interface DBRunnable<T, E> {

    T onDB(Consumer<E> consumer, DbType... dbTypes);

    T elseDB(Consumer<E> consumer);

    void runOnDB(DbType dbType, E e);
}
