package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.db.reflect.FieldInfo;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisLikeQueryParameter;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisParameter;
import cn.mybatis.mp.core.mybatis.typeHandler.LikeQuerySupport;
import db.sql.api.Cmd;
import db.sql.api.cmd.LikeMode;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.Objects;

public class MpDatasetField extends DatasetField {

    private final FieldInfo fieldInfo;

    private final TypeHandler<?> typeHandler;

    private final JdbcType jdbcType;

    public MpDatasetField(IDataset dataset, String name, FieldInfo fieldInfo, TypeHandler<?> typeHandler, JdbcType jdbcType) {
        super(dataset, name);
        this.fieldInfo = fieldInfo;
        this.typeHandler = typeHandler;
        this.jdbcType = jdbcType;
    }

    @Override
    public Object paramWrap(Object param) {
        if (Objects.isNull(param) || param instanceof Cmd) {
            return param;
        }
        if (!this.fieldInfo.getTypeClass().isAssignableFrom(param.getClass())) {
            return param;
        }
        if (Objects.isNull(this.typeHandler)) {
            return param;
        }
        return new MybatisParameter(param, (Class<? extends TypeHandler<?>>) typeHandler.getClass(), this.jdbcType);
    }

    @Override
    public Object likeParamWrap(LikeMode likeMode, Object param, boolean isNotLike) {
        if (Objects.isNull(param) || param instanceof Cmd) {
            return param;
        }
        if (!this.fieldInfo.getTypeClass().isAssignableFrom(param.getClass())) {
            return param;
        }
        if (Objects.isNull(this.typeHandler)) {
            return param;
        }
        if (!(this.typeHandler instanceof LikeQuerySupport)) {
            return param;
        }
        LikeQuerySupport likeQuerySupport = (LikeQuerySupport) this.typeHandler;
        param = new MybatisLikeQueryParameter(param, isNotLike, likeMode, (Class<? extends TypeHandler<?>>) typeHandler.getClass(), this.jdbcType);
        likeMode = likeQuerySupport.convertLikeMode(likeMode, isNotLike);
        return new Object[]{likeMode, param};
    }
}
