package cn.mybatis.mp.core.mybatis.provider;

import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.util.DbTypeUtil;
import db.sql.api.DbType;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class SQLCmdSqlSource implements SqlSource {

    private final static Map<String, BiFunction<Object, ProviderContext, DbType, String>> SQL_GENERATOR_FUN_MAP = new HashMap<>();

    static {
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.QUERY_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.cmdQuery((SQLCmdQueryContext) context, providerContext, dbType));
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.GET_QUERY_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.getCmdQuery((SQLCmdQueryContext) context, providerContext, dbType));
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.COUNT_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.cmdCount((SQLCmdCountQueryContext) context, providerContext, dbType));
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.QUERY_COUNT_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.countFromQuery((SQLCmdCountFromQueryContext) context, providerContext, dbType));
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.UPDATE_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.update((SQLCmdUpdateContext) context, providerContext, dbType));
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.DELETE_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.delete((SQLCmdDeleteContext) context, providerContext, dbType));
        SQL_GENERATOR_FUN_MAP.put(MybatisSQLProvider.SAVE_NAME, (context, providerContext, dbType) -> MybatisSQLProvider.save((SQLCmdInsertContext) context, providerContext, dbType));
    }

    private final Configuration configuration;
    private final Method providerMethod;
    private final ProviderContext providerContext;

    private final DbType dbType;

    public SQLCmdSqlSource(Configuration configuration, Method providerMethod, ProviderContext providerContext) {
        this.configuration = configuration;
        this.providerMethod = providerMethod;
        this.providerContext = providerContext;
        this.dbType = DbTypeUtil.getDbType(configuration);
    }


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        String methodName = providerMethod.getName();
        BiFunction<Object, ProviderContext, DbType, String> sqlGenerator = SQL_GENERATOR_FUN_MAP.get(methodName);
        if (Objects.isNull(sqlGenerator)) {
            throw new RuntimeException("Unadapted: Unknown SQL method: " + methodName);
        }
        String sql = sqlGenerator.apply(parameterObject, this.providerContext, dbType);
        return new BoundSql(this.configuration, sql, Collections.emptyList(), parameterObject);
    }

    public DbType getDbType() {
        return dbType;
    }

    @FunctionalInterface
    public interface BiFunction<T, U, U2, R> {

        R apply(T t, U u, U2 u2);

        default <V> BiFunction<T, U, U2, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (T t, U u, U2 u2) -> after.apply(apply(t, u, u2));
        }
    }
}
