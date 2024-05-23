package com.mybatis.mp.core.test.db2.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {
    public LocalDateTimeTypeHandler() {
    }

    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String v = rs.getString(columnName);
        if (Objects.isNull(v)) {
            return null;
        }
        return (LocalDateTime) rs.getObject(columnName, LocalDateTime.class);
    }

    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        if (Objects.isNull(v)) {
            return null;
        }
        return (LocalDateTime) rs.getObject(columnIndex, LocalDateTime.class);
    }

    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String v = cs.getString(columnIndex);
        if (Objects.isNull(v)) {
            return null;
        }
        return (LocalDateTime) cs.getObject(columnIndex, LocalDateTime.class);
    }
}
