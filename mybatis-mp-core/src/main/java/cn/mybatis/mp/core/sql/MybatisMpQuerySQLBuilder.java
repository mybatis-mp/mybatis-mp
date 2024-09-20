package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.OptimizeOptions;
import db.sql.api.impl.tookit.SQLOptimizeUtils;

public class MybatisMpQuerySQLBuilder implements QuerySQLBuilder {

    /**
     * 构建query sql
     *
     * @param query           查询
     * @param context         上下文
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
     * @param query           查询
     * @param context         上下文
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
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildCountSQLFromQuery(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getCountSqlFromQuery(query, context, optimizeOptions);
    }

    @Override
    public StringBuilder buildInsertSQL(BaseInsert insert, SqlBuilderContext context) {
        return insert.sql(context, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(insert.cmds())));
    }

    @Override
    public StringBuilder buildUpdateSQL(BaseUpdate update, SqlBuilderContext context) {
        return update.sql(context, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(update.cmds())));
    }

    @Override
    public StringBuilder buildDeleteSQL(BaseDelete delete, SqlBuilderContext context) {
        return delete.sql(context, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(delete.cmds())));
    }
}
