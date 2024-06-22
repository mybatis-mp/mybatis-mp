package com.mybatis.mp.core.test.typeHandler;

import cn.mybatis.mp.core.mybatis.typeHandler.LikeQuerySupport;
import db.sql.api.cmd.LikeMode;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptTypeHandler extends BaseTypeHandler<String> implements LikeQuerySupport<String> {

    @Override
    public LikeMode convertLikeMode(LikeMode likeMode, boolean isNotLike) {
        return LikeMode.NONE;
    }

    @Override
    public void setLikeParameter(LikeMode likeMode, boolean isNotLike, PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, "加密like参数");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, "加密参数");
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return "加密后的数据";
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return "加密后的数据";
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return "加密后的数据";
    }
}
