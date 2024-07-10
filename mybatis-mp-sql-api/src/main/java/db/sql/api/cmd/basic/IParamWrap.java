package db.sql.api.cmd.basic;

import db.sql.api.cmd.LikeMode;

public interface IParamWrap {

    default Object paramWrap(Object value) {
        return value;
    }

    /**
     * like 参数包裹
     *
     * @param param
     * @param likeMode
     * @param isNotLike
     * @return 假如是单个对象 说明 like 模式未变 假如返回是数组2位，第1位应为LikeMode 第2位位参数值
     */
    default Object likeParamWrap(LikeMode likeMode, Object param, boolean isNotLike) {
        return param;
    }
}
