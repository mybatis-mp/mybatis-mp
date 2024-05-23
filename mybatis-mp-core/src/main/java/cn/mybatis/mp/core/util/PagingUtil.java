package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.mybatis.provider.PagingCountSqlSource;
import cn.mybatis.mp.core.mybatis.provider.PagingListSqlSource;
import cn.mybatis.mp.db.annotations.Paging;
import db.sql.api.DbType;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PagingUtil {

    public static void handleMappedStatement(MappedStatement ms) {
        if (ms.getSqlCommandType() != SqlCommandType.SELECT) {
            return;
        }

        Method mapperMethod = MappedStatementUtil.getMethod(ms);
        if (Objects.isNull(mapperMethod)) {
            return;
        }

        if (mapperMethod.isDefault() || Modifier.isStatic(mapperMethod.getModifiers())) {
            return;
        }

        if (!mapperMethod.isAnnotationPresent(Paging.class)) {
            return;
        }

        addPagingCountMappedStatement(ms);
        addPagingListMappedStatement(ms, mapperMethod);
    }

    private static void addPagingListMappedStatement(MappedStatement ms, Method mapperMethod) {
        String id = ms.getId() + "-list";

        Class returnType = ms.getResultMaps().get(0).getType();

        ResultMap resultMap;
        if (Pager.class.isAssignableFrom(returnType)) {
            resultMap = new ResultMap.Builder(ms.getConfiguration(), id + "-inline", GenericUtil.getGenericParameterTypes(mapperMethod).get(0), Collections.emptyList()).build();
        } else {
            resultMap = ms.getResultMaps().get(0);
        }

        SqlSource sqlSource = new PagingListSqlSource(ms.getConfiguration(), ms.getSqlSource());
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(ms.getConfiguration(), id, sqlSource, ms.getSqlCommandType())
                .resource(ms.getResource())
                .resultMaps(Collections.singletonList(resultMap))
                .parameterMap(ms.getParameterMap())
                .keyGenerator(ms.getKeyGenerator())
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .lang(ms.getLang())
                .timeout(ms.getTimeout())
                .useCache(ms.isUseCache())
                .cache(ms.getCache());
        MappedStatement newMappedStatement = msBuilder.build();
        try {
            ms.getConfiguration().addMappedStatement(newMappedStatement);
        } catch (IllegalArgumentException e) {
            ms.getStatementLog().warn(e.getMessage());
        }
    }

    private static void addPagingCountMappedStatement(MappedStatement ms) {
        String id = ms.getId() + "-count";

        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), id + "-inline", Integer.TYPE, Collections.emptyList()).build();
        List<ResultMap> resultMaps = Collections.singletonList(resultMap);

        SqlSource sqlSource = new PagingCountSqlSource(ms.getConfiguration(), ms.getSqlSource());
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(ms.getConfiguration(), id, sqlSource, ms.getSqlCommandType())
                .resource(ms.getResource())
                .resultMaps(resultMaps)
                .parameterMap(ms.getParameterMap())
                .keyGenerator(ms.getKeyGenerator())
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .lang(ms.getLang())
                .timeout(ms.getTimeout())
                .useCache(ms.isUseCache())
                .cache(ms.getCache());
        MappedStatement newMappedStatement = msBuilder.build();
        try {
            ms.getConfiguration().addMappedStatement(newMappedStatement);
        } catch (IllegalArgumentException e) {
            ms.getStatementLog().warn(e.getMessage());
        }
    }


    public static String getLimitedSQL(DbType dbType, Pager<?> pager, String sql) {
        if (dbType == DbType.SQL_SERVER) {
            StringBuilder sqlBuilder = new StringBuilder(sql);
            sqlBuilder.append(" OFFSET ").append(pager.getOffset()).append(" ROWS FETCH NEXT ").append(pager.getSize()).append(" ROWS ONLY");
            return sqlBuilder.toString();
        }


        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM (");
        sqlBuilder.append(sql).append(") T ");
        if (dbType == DbType.ORACLE) {
            sqlBuilder.append(" OFFSET ").append(pager.getOffset()).append(" ROWS FETCH NEXT ").append(pager.getSize()).append(" ROWS ONLY");
            return sqlBuilder.toString();
        }
        sqlBuilder.append(" LIMIT ").append(pager.getSize()).append(" OFFSET ").append(pager.getOffset());
        return sqlBuilder.toString();
    }

    public static String getCountSQL(DbType dbType, String sql) {
        if (dbType == DbType.SQL_SERVER) {
            String upperCaseSql = sql.toUpperCase();
            //移除最外层的order by
            int orderByIndex = upperCaseSql.lastIndexOf("ORDER BY");
            if (orderByIndex > 0) {
                int bracketRightIndex = upperCaseSql.lastIndexOf(")", orderByIndex);
                String orderByStr;
                if (bracketRightIndex < 0) {
                    orderByStr = upperCaseSql.substring(orderByIndex);
                } else {
                    orderByStr = upperCaseSql.substring(bracketRightIndex);
                }
                if (!orderByStr.contains("OFFSET")) {
                    if (bracketRightIndex < 0) {
                        sql = sql.substring(0, orderByIndex);
                    } else {
                        sql = sql.substring(0, orderByIndex) + " " + sql.substring(bracketRightIndex);
                    }
                }
            }
        }
        return String.format("SELECT COUNT(*) FROM (%s) T", sql);
    }

}
