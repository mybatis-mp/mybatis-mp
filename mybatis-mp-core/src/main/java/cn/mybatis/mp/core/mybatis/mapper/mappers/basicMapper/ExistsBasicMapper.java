package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.ExistsMethodUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public interface ExistsBasicMapper extends BaseBasicMapper {

    /**
     * 是否存在
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 是否存在
     */
    default <T> boolean exists(Class<T> entityType, Consumer<Where> consumer) {
        return ExistsMethodUtil.exists(getBasicMapper(), Tables.get(entityType), consumer);
    }

    /**
     * 是否存在
     *
     * @param entityType 实体类
     * @param where
     * @return 是否存在
     */
    default <T> boolean exists(Class<T> entityType, Where where) {
        return ExistsMethodUtil.exists(getBasicMapper(), Tables.get(entityType), where);
    }
}
