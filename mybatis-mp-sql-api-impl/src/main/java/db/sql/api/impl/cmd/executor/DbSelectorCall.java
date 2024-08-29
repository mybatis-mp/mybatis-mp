package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;
import db.sql.api.impl.tookit.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class DbSelectorCall<R> implements SelectorCall<R> {

    private final Map<DbType, Callable> consumers = new HashMap<>();

    private Callable<R> otherwise;

    @Override
    public DbSelectorCall<R> when(DbType dbType, Callable<R> runnable) {
        consumers.put(dbType, runnable);
        return this;
    }

    @Override
    public DbSelectorCall<R> when(DbType[] dbTypes, Callable<R> runnable) {
        for (DbType dbType : dbTypes) {
            consumers.put(dbType, runnable);
        }
        return this;
    }

    @Override
    public DbSelectorCall<R> otherwise(Callable<R> runnable) {
        if (Objects.nonNull(this.otherwise)) {
            throw new RuntimeException("The method of 'otherwise' has already called");
        }
        this.otherwise = runnable;
        return this;
    }

    @Override
    public DbSelectorCall<R> otherwise() {
        return this.otherwise(() -> {
            return null;
        });
    }

    private R execute(Callable<R> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public R dbExecute(DbType dbType) {
        Callable<R> runnable = consumers.get(dbType);
        if (Objects.nonNull(runnable)) {
            return this.execute(runnable);
        }
        if (Objects.nonNull(this.otherwise)) {
            return this.execute(this.otherwise);
        }
        throw new RuntimeException("Not adapted to DbType " + dbType);
    }
}
