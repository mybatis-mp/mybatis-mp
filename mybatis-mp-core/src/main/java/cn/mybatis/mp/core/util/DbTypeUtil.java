package cn.mybatis.mp.core.util;

import db.sql.api.DbType;
import org.apache.ibatis.session.Configuration;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Objects;

public final class DbTypeUtil {
    public static DbType getDbType(Configuration configuration) {
        return getDbType(configuration.getDatabaseId(), configuration.getEnvironment().getDataSource());
    }

    public static DbType getDbType(String databaseId, DataSource dataSource) {
        if (Objects.isNull(databaseId) || StringPool.EMPTY.equals(databaseId)) {
            return DbTypeUtil.getDbType(dataSource);
        }
        return DbType.getByName(databaseId);
    }

    public static DbType getDbType(DataSource dataSource) {
        return getDbType(getJdbcUrl(dataSource));
    }

    public static DbType getDbType(String jdbcUrl) {
        jdbcUrl = jdbcUrl.toLowerCase();
        if (jdbcUrl.contains(":mysql:") || jdbcUrl.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (jdbcUrl.contains(":mariadb:")) {
            return DbType.MARIA_DB;
        } else if (jdbcUrl.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (jdbcUrl.contains(":postgresql:")) {
            return DbType.PGSQL;
        } else if (jdbcUrl.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (jdbcUrl.contains(":h2:")) {
            return DbType.H2;
        } else if (jdbcUrl.contains(":dm:")) {
            return DbType.DM;
        } else if (jdbcUrl.contains(":db2:")) {
            return DbType.DB2;
        } else {
            throw new RuntimeException("Unrecognized database type:" + jdbcUrl);
        }
    }

    public static String getJdbcUrl(DataSource dataSource) {
        String[] methodNames = new String[]{"getUrl", "getJdbcUrl"};
        for (String methodName : methodNames) {
            try {
                Method method = dataSource.getClass().getMethod(methodName);
                return (String) method.invoke(dataSource);
            } catch (Exception e) {
                //ignore
            }
        }
        try (Connection connection = dataSource.getConnection()) {
            return connection.getMetaData().getURL();
        } catch (Exception e) {
            throw new RuntimeException("无法解析到 数据库的url");
        }
    }
}
