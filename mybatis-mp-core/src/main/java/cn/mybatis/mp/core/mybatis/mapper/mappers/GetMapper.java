package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.GetMethodUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.function.Consumer;

public interface GetMapper<T> extends BaseMapper<T> {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 单个当前实体类
     */
    default <ID extends Serializable> T getById(ID id) {
        return this.getById(id, (Getter<T>[]) null);
    }

    /**
     * 根据ID查询，只返回指定列
     *
     * @param id           ID
     * @param selectFields select列
     * @return 单个当前实体类
     */
    default <ID extends Serializable> T getById(ID id, Getter<T>... selectFields) {
        return GetMethodUtil.getById(getBasicMapper(), getTableInfo(), id, selectFields);
    }

    /**
     * 单个查询
     *
     * @param consumer where consumer
     * @return 单个当前实体
     */
    default T get(Consumer<Where> consumer) {
        return this.get(consumer, (Getter<T>[]) null);
    }

    /**
     * 单个查询
     *
     * @param consumer     where consumer
     * @param selectFields select列
     * @return 单个当前实体
     */
    default T get(Consumer<Where> consumer, Getter<T>... selectFields) {
        return GetMethodUtil.get(getBasicMapper(), getTableInfo(), consumer, selectFields);
    }

    /**
     * 单个查询
     *
     * @param where where
     * @return 单个当前实体
     */
    default T get(Where where) {
        return get(where, (Getter<T>[]) null);
    }

    /**
     * 单个查询
     *
     * @param where        where
     * @param selectFields select列
     * @return 单个当前实体
     */
    default T get(Where where, Getter<T>... selectFields) {
        return GetMethodUtil.get(getBasicMapper(), getTableInfo(), where, selectFields);
    }
}
