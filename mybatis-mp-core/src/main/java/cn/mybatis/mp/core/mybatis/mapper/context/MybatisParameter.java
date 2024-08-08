package cn.mybatis.mp.core.mybatis.mapper.context;

import db.sql.api.impl.tookit.Objects;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.Serializable;

public class MybatisParameter implements Serializable {

    private final Object value;

    private final Class<? extends TypeHandler<?>> typeHandler;

    private final JdbcType jdbcType;

    public MybatisParameter(Object value, Class<? extends TypeHandler<?>> typeHandler, JdbcType jdbcType) {
        this.typeHandler = typeHandler;
        this.jdbcType = jdbcType;
        this.value = value;
    }

    public static MybatisParameter create(Object value, Class<? extends TypeHandler<?>> typeHandler, JdbcType jdbcType) {
        return new MybatisParameter(value, typeHandler, jdbcType);
    }

    public Class<? extends TypeHandler<?>> getTypeHandler() {
        return typeHandler;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Objects.isNull(value) ? "null" : value.toString();
    }
}
