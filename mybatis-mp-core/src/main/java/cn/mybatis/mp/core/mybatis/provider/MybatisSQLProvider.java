package cn.mybatis.mp.core.mybatis.provider;


import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.DbType;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Objects;

public class MybatisSQLProvider {
    public static final String SAVE_NAME = "save";
    public static final String UPDATE_NAME = "update";
    public static final String DELETE_NAME = "delete";
    public static final String QUERY_NAME = "cmdQuery";
    public static final String GET_QUERY_NAME = "getCmdQuery";
    public static final String COUNT_NAME = "cmdCount";
    public static final String QUERY_COUNT_NAME = "countFromQuery";

    private MybatisSQLProvider() {

    }

    public static String save(SQLCmdInsertContext insertContext, ProviderContext providerContext, DbType dbType) {
        insertContext.init(dbType);
        return insertContext.sql(dbType);
    }

    public static String update(SQLCmdUpdateContext updateContext, ProviderContext providerContext, DbType dbType) {
        updateContext.init(dbType);
        String sql = updateContext.sql(dbType);
        if ((updateContext.getExecution().getWhere() == null || !updateContext.getExecution().getWhere().hasContent()) && (!updateContext.getExecution().getWhere().extConditionChain().hasContent())) {
            throw new RuntimeException("update has no where condition content ");
        }
        return sql;
    }

    public static String delete(SQLCmdDeleteContext deleteContext, ProviderContext providerContext, DbType dbType) {
        deleteContext.init(dbType);
        String sql = deleteContext.sql(dbType);
        if (deleteContext.getExecution().getWhere() == null || !deleteContext.getExecution().getWhere().hasContent()) {
            throw new RuntimeException("delete has no where condition content ");
        }
        return sql;
    }

    /**
     * 处理前缀映射
     *
     * @param queryContext
     */
    private static void handlerPrefixMapping(SQLCmdQueryContext queryContext) {
        BaseQuery query = queryContext.getExecution();
        if (Objects.nonNull(query.getReturnType())) {
            TablePrefixUtil.prefixMapping(query, query.getReturnType());
        }
    }


    public static String countFromQuery(SQLCmdCountFromQueryContext queryContext, ProviderContext providerContext, DbType dbType) {
        queryContext.init(dbType);
        return queryContext.sql(dbType);
    }

    public static String cmdQuery(SQLCmdQueryContext queryContext, ProviderContext providerContext, DbType dbType) {
        queryContext.init(dbType);
        handlerPrefixMapping(queryContext);
        return queryContext.sql(dbType);
    }

    public static String getCmdQuery(SQLCmdQueryContext queryContext, ProviderContext providerContext, DbType dbType) {
        //SQL_SERVER 需要order by 才能分页 所以不加
        if (dbType != DbType.SQL_SERVER) {
            if (Objects.isNull(queryContext.getExecution().getLimit())) {
                queryContext.getExecution().limit(2);
            }
        }
        queryContext.init(dbType);
        handlerPrefixMapping(queryContext);
        return queryContext.sql(dbType);
    }

    public static String cmdCount(SQLCmdCountQueryContext queryContext, ProviderContext providerContext, DbType dbType) {
        queryContext.init(dbType);
        return queryContext.sql(dbType);
    }
}
