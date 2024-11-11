package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.GetMethodUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.function.Consumer;

public interface GetBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 根据ID查询
     * @param entityType 实体类
     * @param id ID
     * @return 单个当前实体类
     */
    default <T, ID extends Serializable> T getById(Class<T> entityType, ID id) {
        return this.getById(entityType, id, (Getter<T>[]) null);
    }

    /**
     * 根据ID查询，只返回指定列
     * @param entityType 实体类
     * @param id           ID
     * @param selectFields select列
     * @return 单个当前实体类
     */
    default <T, ID extends Serializable> T getById(Class<T> entityType, ID id, Getter<T>... selectFields) {
        return GetMethodUtil.getById(getBasicMapper(), Tables.get(entityType), id, selectFields);
    }

    /**
     * 单个查询
     * @param entityType 实体类
     * @param consumer where consumer
     * @return 单个当前实体
     */
    default <T> T get(Class<T> entityType, Consumer<Where> consumer) {
        return this.get(entityType, consumer, (Getter<T>[]) null);
    }

    /**
     * 单个查询
     * @param entityType 实体类
     * @param consumer     where consumer
     * @param selectFields select列
     * @return 单个当前实体
     */
    default <T> T get(Class<T> entityType, Consumer<Where> consumer, Getter<T>... selectFields) {
        return GetMethodUtil.get(getBasicMapper(), Tables.get(entityType), consumer, selectFields);
    }

    /**
     * 单个查询
     * @param entityType 实体类
     * @param where where
     * @return 单个当前实体
     */
    default <T> T get(Class<T> entityType, Where where) {
        return get(entityType, where, (Getter<T>[]) null);
    }

    /**
     * 单个查询
     * @param entityType 实体类
     * @param where        where
     * @param selectFields select列
     * @return 单个当前实体
     */
    default <T> T get(Class<T> entityType, Where where, Getter<T>... selectFields) {
        return GetMethodUtil.get(getBasicMapper(), Tables.get(entityType), where, selectFields);
    }
}
