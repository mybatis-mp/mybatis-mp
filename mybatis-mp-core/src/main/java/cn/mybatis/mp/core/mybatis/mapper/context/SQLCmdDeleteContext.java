package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.mybatis.provider.MybatisSqlBuilderContext;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import db.sql.api.DbType;
import db.sql.api.SQLMode;

import java.util.Objects;

public class SQLCmdDeleteContext extends BaseSQLCmdContext<BaseDelete> {

    public SQLCmdDeleteContext(BaseDelete delete) {
        super(delete);
    }

    @Override
    public String sql(DbType dbType) {
        if (Objects.nonNull(sql)) {
            return sql;
        }
        sqlBuilderContext = new MybatisSqlBuilderContext(dbType, SQLMode.PREPARED);
        sql = MybatisMpConfig.getQuerySQLBuilder().buildDeleteSQL(getExecution(), sqlBuilderContext).toString();
        return sql;
    }
}
