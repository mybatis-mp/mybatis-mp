package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.CountMethodUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public interface CountBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 总数
     * @param entityType 实体类
     * @return count数
     */
    default <T> int countAll(Class<T> entityType) {
        return CountMethodUtil.countAll(getBasicMapper(), Tables.get(entityType));
    }

    /**
     * 是否存在
     * @param entityType 实体类
     * @param consumer where consumer
     * @return count数
     */
    default <T> int count(Class<T> entityType, Consumer<Where> consumer) {
        return CountMethodUtil.count(getBasicMapper(), Tables.get(entityType), consumer);
    }

    /**
     * 是否存在
     * @param entityType 实体类
     * @param where where
     * @return count数
     */
    default <T> int count(Class<T> entityType, Where where) {
        return CountMethodUtil.count(getBasicMapper(), Tables.get(entityType), where);
    }
}
