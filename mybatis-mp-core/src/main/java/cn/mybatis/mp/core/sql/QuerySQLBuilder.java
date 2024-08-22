package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.OptimizeOptions;

public interface QuerySQLBuilder {

    /**
     * 构建query sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    StringBuilder buildQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions);


    /**
     * 构建 count query sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    StringBuilder buildCountQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions);

    /**
     * 从query 中 获取 count 查询 sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    StringBuilder buildCountSQLFromQuery(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions);

}
