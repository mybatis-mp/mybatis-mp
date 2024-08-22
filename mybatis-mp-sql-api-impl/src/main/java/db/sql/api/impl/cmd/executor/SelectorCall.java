package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;

import java.util.concurrent.Callable;

/**
 * 选择器 不同数据库执行不同的方法
 */
public interface SelectorCall<R> {

    /**
     * 当数据库类型为dbType 时
     *
     * @param dbType   数据库类型
     * @param runnable 执行器
     * @return 自己
     */
    SelectorCall<R> when(DbType dbType, Callable<R> runnable);

    /**
     * 当数据库类型在dbTypes 时
     *
     * @param dbTypes  数据库类型数组
     * @param runnable 执行器
     * @return 自己
     */
    default SelectorCall<R> when(DbType[] dbTypes, Callable<R> runnable) {
        for (DbType dbType : dbTypes) {
            when(dbType, runnable);
        }
        return this;
    }

    /**
     * 其他数据库类型时
     *
     * @param runnable 执行器
     * @return 自己
     */
    SelectorCall<R> otherwise(Callable<R> runnable);

    /**
     * 其他数据库类型时 忽略
     */
    SelectorCall<R> otherwise();

    /**
     * 执行
     *
     * @param dbType 数据库类型
     */
    R dbExecute(DbType dbType);
}
