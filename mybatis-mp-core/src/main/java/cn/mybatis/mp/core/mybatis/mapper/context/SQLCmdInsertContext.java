package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.mybatis.provider.MybatisSqlBuilderContext;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import db.sql.api.DbType;
import db.sql.api.SQLMode;

import java.util.Objects;

public class SQLCmdInsertContext<T extends BaseInsert> extends BaseSQLCmdContext<T> {

    protected Class<?> entityType;

    public SQLCmdInsertContext() {

    }

    public SQLCmdInsertContext(T t) {
        super(t);
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    @Override
    public String sql(DbType dbType) {
        if (Objects.nonNull(sql)) {
            return sql;
        }
        sqlBuilderContext = new MybatisSqlBuilderContext(dbType, SQLMode.PREPARED);
        sql = MybatisMpConfig.getQuerySQLBuilder().buildInsertSQL(getExecution(), sqlBuilderContext).toString();
        return sql;
    }
}
