package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.OptimizeOptions;
import db.sql.api.impl.tookit.SQLOptimizeUtils;

public class MybatisMpQuerySQLBuilder implements QuerySQLBuilder {

    /**
     * 构建query sql
     *
     * @param query    查询
     * @param context  上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getOptimizedSql(query, context, optimizeOptions);
    }

    /**
     * 构建count查询sql
     *
     * @param query    查询
     * @param context  上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildCountQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getOptimizedCountSql(query, context, optimizeOptions);
    }

    /**
     * 从query中构建count sql，一般用于分页时使用
     *
     * @param query    查询
     * @param context  上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildCountSQLFromQuery(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getCountSqlFromQuery(query, context, optimizeOptions);
    }
}
