package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.CursorMethodUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

public interface CursorBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType 实体类
     * @param ids        指定ID
     * @param <ID>
     * @return 返回结果列表
     */
    default <T, ID extends Serializable> Cursor<T> cursorByIds(Class<T> entityType, ID[] ids) {
        return this.cursorByIds(entityType, ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType   实体类
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>
     * @return 返回结果列表
     */
    default <T, ID extends Serializable> Cursor<T> cursorByIds(Class<T> entityType, ID[] ids, Getter<T>... selectFields) {
        return CursorMethodUtil.cursorByIds(getBasicMapper(), Tables.get(entityType), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType 实体类
     * @param ids        指定ID
     * @param <ID>
     * @return 返回结果列表
     */
    default <T, ID extends Serializable> Cursor<T> cursorByIds(Class<T> entityType, Collection<ID> ids) {
        return this.cursorByIds(entityType, ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType   实体类
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>
     * @return 返回结果列表
     */
    default <T, ID extends Serializable> Cursor<T> cursorByIds(Class<T> entityType, Collection<ID> ids, Getter<T>... selectFields) {
        return CursorMethodUtil.cursorByIds(getBasicMapper(), Tables.get(entityType), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 返回结果列表
     */
    default <T> Cursor<T> cursor(Class<T> entityType, Consumer<Where> consumer) {
        return this.cursor(entityType, consumer, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType   实体类
     * @param consumer     where consumer
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    default <T> Cursor<T> cursor(Class<T> entityType, Consumer<Where> consumer, Getter<T>... selectFields) {
        return CursorMethodUtil.cursor(getBasicMapper(), Tables.get(entityType), consumer, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType 实体类
     * @param where      where
     * @return 返回结果列表
     */
    default <T> Cursor<T> cursor(Class<T> entityType, Where where) {
        return this.cursor(entityType, where, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param entityType   实体类
     * @param where        where
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    default <T> Cursor<T> cursor(Class<T> entityType, Where where, Getter<T>... selectFields) {
        return CursorMethodUtil.cursor(getBasicMapper(), Tables.get(entityType), where, selectFields);
    }

    /**
     * 查询所有
     *
     * @param entityType 实体类
     * @param <T>        实体类
     * @return 总Cursor
     */
    default <T> Cursor<T> cursorAll(Class<T> entityType) {
        return CursorMethodUtil.cursorAll(getBasicMapper(), Tables.get(entityType));
    }
}
