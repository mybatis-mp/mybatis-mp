/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.exception.DbTypeParseException;
import db.sql.api.DbType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Objects;

@Slf4j
public final class DbTypeUtil {

    public static DbType getDbType(Configuration configuration) {
        return getDbType(configuration, null);
    }

    public static DbType getDbType(DataSource dataSource) {
        return getDbType(dataSource, null);
    }

    public static DbType getDbType(String databaseId, DataSource dataSource) {
        if (Objects.isNull(databaseId) || databaseId.isEmpty()) {
            return DbTypeUtil.getDbType(dataSource, null);
        }
        return DbType.getByName(databaseId);
    }

    public static DbType getDbType(Configuration configuration, DbType defaultDbType) {
        try {
            return getDbType(configuration.getDatabaseId(), configuration.getEnvironment().getDataSource());
        } catch (DbTypeParseException e) {
            return defaultDbType;
        }
    }

    public static DbType getDbType(DataSource dataSource, DbType defaultDbType) {
        return getDbType(getJdbcUrl(dataSource), defaultDbType);
    }

    public static DbType getDbType(String jdbcUrl, DbType defaultDbType) {
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
        } else if (jdbcUrl.contains(":kingbase8:")) {
            return DbType.KING_BASE;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        }
        if (defaultDbType == null) {
            throw new DbTypeParseException("Unrecognized database type:" + jdbcUrl);
        }
        return defaultDbType;
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
            return getJdbcUrl(connection);
        } catch (Exception e) {
            throw new RuntimeException("无法解析到 数据库的url", e);
        }
    }

    public static String getJdbcUrl(Connection connection) {
        try {
            return connection.getMetaData().getURL();
        } catch (Exception e) {
            throw new RuntimeException("无法解析到 数据库的url", e);
        }
    }

    public static DbType getDbType(Connection connection) {
        return getDbType(connection, null);
    }

    public static DbType getDbType(Connection connection, DbType defaultDbType) {
        return getDbType(getJdbcUrl(connection), defaultDbType);
    }
}
