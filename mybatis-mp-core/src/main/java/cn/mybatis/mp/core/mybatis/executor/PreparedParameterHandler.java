package cn.mybatis.mp.core.mybatis.executor;

import cn.mybatis.mp.core.mybatis.configuration.MybatisConfiguration;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisLikeQueryParameter;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisParameter;
import cn.mybatis.mp.core.mybatis.typeHandler.LikeQuerySupport;
import cn.mybatis.mp.core.mybatis.typeHandler.MybatisTypeHandlerUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.function.Supplier;

public class PreparedParameterHandler implements ParameterHandler {

    private final PreparedParameterContext parameterContext;

    private final MybatisConfiguration configuration;

    public PreparedParameterHandler(MybatisConfiguration configuration, PreparedParameterContext parameterContext) {
        this.configuration = configuration;
        this.parameterContext = parameterContext;
    }

    @Override
    public Object getParameterObject() {
        return parameterContext;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        Object[] params = parameterContext.getParameters();
        int length = params.length;
        for (int i = 0; i < length; i++) {
            Object value = params[i];
            if (Objects.isNull(value)) {
                ps.setNull(i + 1, Types.NULL);
                continue;
            }
            if (value instanceof MybatisLikeQueryParameter) {
                MybatisLikeQueryParameter parameter = (MybatisLikeQueryParameter) value;
                Object realValue = parameter.getValue();
                if (value instanceof Supplier) {
                    realValue = ((Supplier<?>) value).get();
                }
                if (Objects.isNull(realValue)) {
                    ps.setNull(i + 1, Types.NULL);
                    continue;
                }
                LikeQuerySupport typeHandler = (LikeQuerySupport) MybatisTypeHandlerUtil.getTypeHandler(this.configuration, realValue.getClass(), parameter.getTypeHandler());
                JdbcType jdbcType = parameter.getJdbcType();
                if (jdbcType == JdbcType.UNDEFINED && realValue.getClass().isEnum()) {
                    jdbcType = null;
                }
                typeHandler.setLikeParameter(parameter.getLikeMode(), parameter.isNotLike(), ps, i + 1, realValue, jdbcType);
            } else if (value instanceof MybatisParameter) {
                MybatisParameter parameter = (MybatisParameter) value;
                Object realValue = parameter.getValue();
                if (value instanceof Supplier) {
                    realValue = ((Supplier<?>) value).get();
                }
                if (Objects.isNull(realValue)) {
                    ps.setNull(i + 1, Types.NULL);
                    continue;
                }
                TypeHandler typeHandler = MybatisTypeHandlerUtil.getTypeHandler(this.configuration, realValue.getClass(), parameter.getTypeHandler());
                JdbcType jdbcType = parameter.getJdbcType();
                if (jdbcType == JdbcType.UNDEFINED && realValue.getClass().isEnum()) {
                    jdbcType = null;
                }
                typeHandler.setParameter(ps, i + 1, realValue, jdbcType);
            } else {
                TypeHandler typeHandler = configuration.getTypeHandlerRegistry().getTypeHandler(value.getClass());
                if (typeHandler != null) {
                    JdbcType jdbcType = JdbcType.UNDEFINED;
                    if (value.getClass().isEnum()) {
                        jdbcType = null;
                    }
                    typeHandler.setParameter(ps, i + 1, value, jdbcType);
                } else {
                    ps.setObject(i + 1, value);
                }
            }
        }
    }
}
