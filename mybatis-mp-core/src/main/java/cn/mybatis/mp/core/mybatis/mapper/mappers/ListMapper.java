package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.ListMethodUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface ListMapper<T> extends BaseMapper, BaseMybatisMapper<T> {

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids  指定ID
     * @param <ID>
     * @return 返回结果列表
     */
    default <ID extends Serializable> List<T> listByIds(ID... ids) {
        return this.listByIds(ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>
     * @return 返回结果列表
     */
    default <ID extends Serializable> List<T> listByIds(ID[] ids, Getter<T>... selectFields) {
        return ListMethodUtil.listByIds(getBasicMapper(), getTableInfo(), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids  指定ID
     * @param <ID>
     * @return 返回结果列表
     */
    default <ID extends Serializable> List<T> listByIds(Collection<ID> ids) {
        return this.listByIds(ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>
     * @return 返回结果列表
     */
    default <ID extends Serializable> List<T> listByIds(Collection<ID> ids, Getter<T>... selectFields) {
        return ListMethodUtil.listByIds(getBasicMapper(), getTableInfo(), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer where consumer
     * @return 返回结果列表
     */
    default List<T> list(Consumer<Where> consumer) {
        return this.list(consumer, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer     where consumer
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    default List<T> list(Consumer<Where> consumer, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), null, consumer, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit    条数
     * @param consumer where consumer
     * @return 返回结果列表
     */
    default List<T> list(Integer limit, Consumer<Where> consumer) {
        return this.list(limit, consumer, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit        条数
     * @param consumer     where consumer
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    default List<T> list(Integer limit, Consumer<Where> consumer, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), limit, consumer, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param where where
     * @return 返回结果列表
     */
    default List<T> list(Where where) {
        return this.list(where, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param where        where
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    default List<T> list(Where where, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), null, where, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit 条数
     * @param where where
     * @return 返回结果列表
     */
    default List<T> list(Integer limit, Where where) {
        return this.list(limit, where, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit        条数
     * @param where        where
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    default List<T> list(Integer limit, Where where, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), limit, where, selectFields);
    }

    default List<T> listAll() {
        return ListMethodUtil.listAll(getBasicMapper(), getTableInfo());
    }
}
