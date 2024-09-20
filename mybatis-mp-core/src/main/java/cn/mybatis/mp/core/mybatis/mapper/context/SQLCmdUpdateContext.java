package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.mybatis.provider.MybatisSqlBuilderContext;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.DbType;
import db.sql.api.SQLMode;

import java.util.Objects;

public class SQLCmdUpdateContext extends BaseSQLCmdContext<BaseUpdate> {

    public SQLCmdUpdateContext(BaseUpdate update) {
        super(update);
    }

    @Override
    public String sql(DbType dbType) {
        if (Objects.nonNull(sql)) {
            return sql;
        }
        sqlBuilderContext = new MybatisSqlBuilderContext(dbType, SQLMode.PREPARED);
        sql = MybatisMpConfig.getQuerySQLBuilder().buildUpdateSQL(getExecution(), sqlBuilderContext).toString();
        return sql;
    }
}
