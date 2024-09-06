package cn.mybatis.mp.core.mybatis.provider;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.util.DbTypeUtil;
import cn.mybatis.mp.core.util.PagingUtil;
import db.sql.api.DbType;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.Objects;

public class PagingListSqlSource implements SqlSource {

    private final Configuration configuration;
    private final SqlSource sqlSource;
    private DbType dbType;

    public PagingListSqlSource(Configuration configuration, SqlSource sqlSource) {
        this.configuration = configuration;
        this.sqlSource = sqlSource;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        Pager<?> pager;
        if (parameterObject instanceof Pager) {
            pager = (Pager<?>) parameterObject;
        } else {
            Map<String, Object> params = (Map<String, Object>) parameterObject;
            pager = (Pager<?>) params.get("arg0");
        }
        return new PagingBoundSql(this.configuration, PagingUtil.getLimitedSQL(getDbType(), pager, sql), boundSql);
    }

    public DbType getDbType() {
        if (Objects.isNull(dbType)) {
            this.dbType = DbTypeUtil.getDbType(configuration);
        }
        return dbType;
    }
}
