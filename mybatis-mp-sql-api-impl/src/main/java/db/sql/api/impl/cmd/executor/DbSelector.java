package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;
import db.sql.api.cmd.executor.DbRunnable;
import db.sql.api.impl.tookit.Objects;

import java.util.HashMap;
import java.util.Map;

public class DbSelector {

    private final Map<DbType, DbRunnable> consumers = new HashMap<>();

    private DbRunnable otherwise;

    public DbSelector when(DbType dbType, DbRunnable runnable) {
        consumers.put(dbType, runnable);
        return this;
    }

    public DbSelector when(DbType[] dbTypes, DbRunnable runnable) {
        for (DbType dbType : dbTypes) {
            consumers.put(dbType, runnable);
        }
        return this;
    }

    public DbSelector otherwise(DbRunnable runnable) {
        this.otherwise = runnable;
        return null;
    }

    public DbSelector otherwise() {
        this.otherwise = () -> {
        };
        return null;
    }

    public void dbExecute(DbType dbType) {
        DbRunnable runnable = consumers.get(dbType);
        if (Objects.nonNull(runnable)) {
            runnable.run();
            return;
        }
        if (Objects.nonNull(this.otherwise)) {
            this.otherwise.run();
            return;
        }
        throw new RuntimeException("Not adapted to DbType" + dbType);
    }
}
