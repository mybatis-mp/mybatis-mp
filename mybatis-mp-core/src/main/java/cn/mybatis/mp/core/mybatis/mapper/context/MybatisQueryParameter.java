package cn.mybatis.mp.core.mybatis.mapper.context;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class MybatisQueryParameter extends MybatisParameter {

    public MybatisQueryParameter(Object value, Class<? extends TypeHandler<?>> typeHandler, JdbcType jdbcType) {
        super(value, typeHandler, jdbcType);
    }

}
