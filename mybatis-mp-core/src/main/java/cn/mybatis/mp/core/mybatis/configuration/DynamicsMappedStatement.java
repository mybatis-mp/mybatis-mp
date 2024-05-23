package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdQueryContext;
import cn.mybatis.mp.core.mybatis.provider.SQLCmdSqlSource;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Collections;
import java.util.Objects;

public class DynamicsMappedStatement {

    public static MappedStatement wrapMappedStatement(MappedStatement ms, Object parameterObject) {
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            //这里是通用mapper 需要在运行时处理
            if (!(parameterObject instanceof SQLCmdInsertContext)) {
                return ms;
            }
            SQLCmdInsertContext sqlCmdInsertContext = (SQLCmdInsertContext) parameterObject;
            if (Objects.isNull(sqlCmdInsertContext.getEntityType())) {
                return ms;
            }

            SQLCmdSqlSource sqlCmdSqlSource = (SQLCmdSqlSource) ms.getSqlSource();

            String id = ms.getId() + sqlCmdSqlSource.getDbType() + sqlCmdInsertContext.getEntityType();

            if (ms.getConfiguration().hasStatement(id)) {
                return ms.getConfiguration().getMappedStatement(id);
            }

            MappedStatement.Builder msBuilder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(), ms.getSqlCommandType())
                    .resource(ms.getResource())
                    .resultMaps(ms.getResultMaps())
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
                TableIdGeneratorWrapper.addEntityKeyGenerator(newMappedStatement, sqlCmdInsertContext.getEntityType());
                if (ms.getConfiguration().hasStatement(id)) {
                    return ms.getConfiguration().getMappedStatement(id);
                }
                ms.getConfiguration().addMappedStatement(newMappedStatement);
                return newMappedStatement;
            } catch (IllegalArgumentException e) {
                ms.getStatementLog().warn(e.getMessage());
            }
            return ms;
        } else if (ms.getSqlCommandType() != SqlCommandType.SELECT) {
            return ms;
        } else if (!(parameterObject instanceof SQLCmdQueryContext)) {
            return ms;
        } else if (ms.getResultMaps().get(0).getType() != Object.class && !ms.getId().endsWith(MybatisMapperProxy.MAP_WITH_KEY_METHOD_NAME)) {
            return ms;
        }
        SQLCmdQueryContext queryContext = (SQLCmdQueryContext) parameterObject;
        if (Objects.isNull(queryContext.getExecution().getReturnType())) {
            return ms;
        }
        return create(queryContext.getExecution().getReturnType(), ms);
    }

    public static MappedStatement create(Class returnTypeClass, MappedStatement ms) {
        String id = ms.getId() + "-" + returnTypeClass.getName();
        if (ms.getConfiguration().hasStatement(id)) {
            return ms.getConfiguration().getMappedStatement(id);
        }
        ResultMap resultMap;
        String resultMapId = returnTypeClass.getName();
        if (ms.getConfiguration().hasResultMap(resultMapId)) {
            resultMap = ms.getConfiguration().getResultMap(resultMapId);
        } else {
            resultMap = new ResultMap.Builder(ms.getConfiguration(), resultMapId, returnTypeClass, Collections.emptyList(), false).build();
        }
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource())
                .resultMaps(ResultMapWrapper.replaceResultMap((MybatisConfiguration) ms.getConfiguration(), Collections.singletonList(resultMap)))
                .parameterMap(ms.getParameterMap())
                .keyGenerator(NoKeyGenerator.INSTANCE)
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .lang(ms.getLang())
                .timeout(ms.getTimeout())
                .useCache(ms.isUseCache())
                .cache(ms.getCache());
        MappedStatement newMappedStatement = msBuilder.build();
        try {
            if (ms.getConfiguration().hasStatement(id)) {
                return ms.getConfiguration().getMappedStatement(id);
            }
            ms.getConfiguration().addMappedStatement(newMappedStatement);
        } catch (IllegalArgumentException e) {
            ms.getStatementLog().warn(e.getMessage());
        }

        return newMappedStatement;

    }
}
