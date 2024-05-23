package com.mybatis.mp.core.test.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneTypeHandler extends BaseTypeHandler<String> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        throw new RuntimeException("not support");
    }

    private String format(String value) throws SQLException {
        if (value != null) {
            return "xxxx123";
        }
        return "null-phone";
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return format(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return format(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return format(cs.getString(columnIndex));
    }
}
