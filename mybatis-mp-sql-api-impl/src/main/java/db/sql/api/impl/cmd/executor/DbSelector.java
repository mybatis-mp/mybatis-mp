package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;
import db.sql.api.cmd.executor.Runnable;
import db.sql.api.impl.tookit.Objects;

import java.util.HashMap;
import java.util.Map;

public class DbSelector implements Selector {

    private final Map<DbType, Runnable> consumers = new HashMap<>();

    private Runnable otherwise;

    public DbSelector when(DbType dbType, Runnable runnable) {
        consumers.put(dbType, runnable);
        return this;
    }

    public DbSelector when(DbType[] dbTypes, Runnable runnable) {
        for (DbType dbType : dbTypes) {
            consumers.put(dbType, runnable);
        }
        return this;
    }

    public DbSelector otherwise(Runnable runnable) {
        this.otherwise = runnable;
        return null;
    }

    public DbSelector otherwise() {
        this.otherwise = () -> {
        };
        return null;
    }

    public void dbExecute(DbType dbType) {
        Runnable runnable = consumers.get(dbType);
        if (Objects.nonNull(runnable)) {
            runnable.run();
            return;
        }
        if (Objects.nonNull(this.otherwise)) {
            this.otherwise.run();
            return;
        }
        throw new RuntimeException("Not adapted to DbType " + dbType);
    }
}
