package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.mybatis.provider.MybatisSqlBuilderContext;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.DbType;
import db.sql.api.SQLMode;

import java.util.Objects;

public class SQLCmdQueryContext extends BaseSQLCmdContext<BaseQuery> {

    private final boolean optimize;

    public SQLCmdQueryContext(BaseQuery execution, boolean optimize) {
        super(execution);
        this.optimize = optimize;
    }

    @Override
    public String sql(DbType dbType) {
        if (Objects.nonNull(sql)) {
            return sql;
        }
        sqlBuilderContext = new MybatisSqlBuilderContext(dbType, SQLMode.PREPARED);
        sql = MybatisMpConfig.getQuerySQLBuilder().buildQuerySQl(getExecution(), sqlBuilderContext, this.optimize).toString();
        return sql;
    }

    public boolean isOptimize() {
        return optimize;
    }
}
