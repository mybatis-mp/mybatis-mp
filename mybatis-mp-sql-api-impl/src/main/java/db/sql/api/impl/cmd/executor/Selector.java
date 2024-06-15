package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;
import db.sql.api.cmd.executor.Runnable;

/**
 * 数据库类型选择器
 */
public interface Selector {

    Selector when(DbType dbType, Runnable runnable);

    default Selector when(DbType[] dbTypes, Runnable runnable) {
        for (DbType dbType : dbTypes) {
            when(dbType, runnable);
        }
        return this;
    }

     Selector otherwise(Runnable runnable);

     Selector otherwise();

     void dbExecute(DbType dbType);
}
