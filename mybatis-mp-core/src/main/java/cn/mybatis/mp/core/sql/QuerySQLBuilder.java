package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.OptimizeOptions;

public interface QuerySQLBuilder {

    /**
     * 构建query sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return sql
     */
    StringBuilder buildQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions);


    /**
     * 构建 count query sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return sql
     */
    StringBuilder buildCountQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions);

    /**
     * 从query 中 获取 count 查询 sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return sql
     */
    StringBuilder buildCountSQLFromQuery(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions);

    /**
     * @param insert  插入对象
     * @param context 上下文
     * @return sql
     */
    StringBuilder buildInsertSQL(BaseInsert insert, SqlBuilderContext context);

    /**
     * @param update  修改对象
     * @param context 上下文
     * @return sql
     */
    StringBuilder buildUpdateSQL(BaseUpdate update, SqlBuilderContext context);

    /**
     * @param delete  删除对象
     * @param context 上下文
     * @return sql
     */
    StringBuilder buildDeleteSQL(BaseDelete delete, SqlBuilderContext context);

}
