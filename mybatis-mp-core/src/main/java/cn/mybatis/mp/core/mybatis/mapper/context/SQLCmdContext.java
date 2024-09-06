package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.mybatis.configuration.PreparedParameterContext;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.executor.Executor;

public interface SQLCmdContext<E extends Executor> extends PreparedParameterContext {

    E getExecution();

    default void init(DbType dbType) {

    }

    String sql(DbType dbType);

    Object[] getParameters();
}
