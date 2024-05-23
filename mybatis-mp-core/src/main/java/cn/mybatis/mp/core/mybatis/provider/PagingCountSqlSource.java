package cn.mybatis.mp.core.mybatis.provider;

import cn.mybatis.mp.core.util.DbTypeUtil;
import cn.mybatis.mp.core.util.PagingUtil;
import db.sql.api.DbType;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

public class PagingCountSqlSource implements SqlSource {

    private final Configuration configuration;

    private final DbType dbType;

    private final SqlSource sqlSource;

    public PagingCountSqlSource(Configuration configuration, SqlSource sqlSource) {
        this.configuration = configuration;
        this.sqlSource = sqlSource;
        this.dbType = DbTypeUtil.getDbType(configuration.getDatabaseId(), configuration.getEnvironment().getDataSource());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        String sql = PagingUtil.getCountSQL(this.dbType, boundSql.getSql());
        return new PagingBoundSql(this.configuration, sql, boundSql);
    }


    public DbType getDbType() {
        return dbType;
    }
}
