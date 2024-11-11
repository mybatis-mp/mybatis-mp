package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.ExistsMethodUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public interface ExistsMapper<T> extends BaseMapper<T> {

    /**
     * 是否存在
     *
     * @param consumer where consumer
     * @return 是否存在
     */
    default boolean exists(Consumer<Where> consumer) {
        return ExistsMethodUtil.exists(getBasicMapper(), getTableInfo(), consumer);
    }

    /**
     * 是否存在
     *
     * @param where
     * @return 是否存在
     */
    default boolean exists(Where where) {
        return ExistsMethodUtil.exists(getBasicMapper(), getTableInfo(), where);
    }
}
