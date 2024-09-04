package cn.mybatis.mp.core.mybatis.provider;

import cn.mybatis.mp.core.util.DbTypeUtil;
import cn.mybatis.mp.core.util.PagingUtil;
import db.sql.api.DbType;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.Objects;

public class PagingCountSqlSource implements SqlSource {

    private final Configuration configuration;
    private final SqlSource sqlSource;
    private final boolean optimize;
    private DbType dbType;

    public PagingCountSqlSource(Configuration configuration, SqlSource sqlSource, boolean optimize) {
        this.configuration = configuration;
        this.sqlSource = sqlSource;
        this.optimize = optimize;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        String sql = PagingUtil.getCountSQL(getDbType(), boundSql.getSql(), optimize);
        return new PagingBoundSql(this.configuration, sql, boundSql);
    }


    public DbType getDbType() {
        if (Objects.isNull(dbType)) {
            this.dbType = DbTypeUtil.getDbType(configuration);
        }
        return dbType;
    }
}
