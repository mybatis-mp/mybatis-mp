package cn.mybatis.mp.core.mybatis.typeHandler;

import db.sql.api.cmd.LikeMode;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface LikeQuerySupport<T> {

    /**
     * likeMode 转换，用于需要转换like的方式
     *
     * @param likeMode  like模式
     * @param isNotLike 是否notLike
     * @return
     */
    default LikeMode convertLikeMode(LikeMode likeMode, boolean isNotLike) {
        return likeMode;
    }

    void setLikeParameter(LikeMode likeMode, boolean isNotLike, PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;
}
