package cn.mybatis.mp.core.mybatis.mapper.context;


import db.sql.api.DbType;
import db.sql.api.impl.cmd.executor.Executor;

public interface SQLCmdContext<E extends Executor> {

    E getExecution();

    default void init(DbType dbType) {

    }

    String sql(DbType dbType);

    Object[] getSQLCmdParams();
}
