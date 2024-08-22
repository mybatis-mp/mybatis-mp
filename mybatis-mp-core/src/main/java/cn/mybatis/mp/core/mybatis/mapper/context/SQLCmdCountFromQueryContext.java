package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.mybatis.provider.MybatisSqlBuilderContext;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.DbType;
import db.sql.api.SQLMode;

import java.util.Objects;

public class SQLCmdCountFromQueryContext extends SQLCmdQueryContext {


    public SQLCmdCountFromQueryContext(BaseQuery execution) {
        super(execution);
    }

    @Override
    public String sql(DbType dbType) {
        if (Objects.nonNull(sql)) {
            return sql;
        }
        sqlBuilderContext = new MybatisSqlBuilderContext(dbType, SQLMode.PREPARED);
        sql = MybatisMpConfig.getQuerySQLBuilder().buildCountSQLFromQuery(getExecution(), sqlBuilderContext, getExecution().getOptimizeOptions()).toString();
        return sql;
    }
}
