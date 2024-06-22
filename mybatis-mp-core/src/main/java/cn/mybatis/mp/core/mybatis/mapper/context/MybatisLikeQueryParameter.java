package cn.mybatis.mp.core.mybatis.mapper.context;

import db.sql.api.cmd.LikeMode;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class MybatisLikeQueryParameter extends MybatisQueryParameter {

    private final boolean notLike;

    private final LikeMode likeMode;

    public MybatisLikeQueryParameter(Object value, boolean isNotLike, LikeMode likeMode, Class<? extends TypeHandler<?>> typeHandler, JdbcType jdbcType) {
        super(value, typeHandler, jdbcType);
        this.notLike = isNotLike;
        this.likeMode = likeMode;
    }

    public boolean isNotLike() {
        return notLike;
    }

    public LikeMode getLikeMode() {
        return likeMode;
    }
}
