package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.mybatis.provider.MybatisSqlBuilderContext;
import db.sql.api.DbType;
import db.sql.api.SQLMode;
import db.sql.api.impl.cmd.executor.Executor;
import db.sql.api.impl.tookit.SQLOptimizeUtils;

import java.util.Objects;

public abstract class BaseSQLCmdContext<E extends Executor> implements SQLCmdContext<E> {

    protected MybatisSqlBuilderContext sqlBuilderContext;

    protected String sql;
    protected E execution;

    private DbType dbType;

    public BaseSQLCmdContext() {

    }

    public BaseSQLCmdContext(E execution) {
        this.execution = execution;
    }

    @Override
    public E getExecution() {
        return execution;
    }

    @Override
    public void init(DbType dbType) {
        this.dbType = dbType;
    }

    @Override
    public String sql(DbType dbType) {
        if (Objects.nonNull(sql)) {
            return sql;
        }
        sqlBuilderContext = new MybatisSqlBuilderContext(dbType, SQLMode.PREPARED);
        sql = getExecution().sql(sqlBuilderContext, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(getExecution().cmds()))).toString();
        return sql;
    }

    @Override
    public Object[] getSQLCmdParams() {
        return sqlBuilderContext.getParams();
    }

    public DbType getDbType() {
        return dbType;
    }
}
