package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

public final class CursorMethodUtil {

    private static <T> Cursor<T> cursorByIds(BasicMapper basicMapper, TableInfo tableInfo, Getter<T>[] selectFields, Consumer<Where> whereConsumer) {
        return cursor(basicMapper, tableInfo, WhereUtil.create(tableInfo, whereConsumer), selectFields);
    }

    public static <T> Cursor<T> cursorByIds(BasicMapper basicMapper, TableInfo tableInfo, Serializable[] ids, Getter<T>[] selectFields) {
        return cursorByIds(basicMapper, tableInfo, selectFields, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }

    public static <T, ID extends Serializable> Cursor<T> cursorByIds(BasicMapper basicMapper, TableInfo tableInfo, Collection<ID> ids, Getter<T>[] selectFields) {
        return cursorByIds(basicMapper, tableInfo, selectFields, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }

    public static <T> Cursor<T> cursor(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer) {
        return cursor(basicMapper, tableInfo, consumer, null);
    }

    public static <T> Cursor<T> cursor(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer, Getter<T>[] selectFields) {
        return cursor(basicMapper, tableInfo, WhereUtil.create(tableInfo, consumer), selectFields);
    }

    public static <T> Cursor<T> cursor(BasicMapper basicMapper, TableInfo tableInfo, Where where, Getter<T>[] selectFields) {
        return basicMapper.cursor(QueryUtil.buildNoOptimizationQuery(tableInfo, where, q -> QueryUtil.fillQueryDefault(q, tableInfo, selectFields)));
    }

    public static <T> Cursor<T> cursorAll(BasicMapper basicMapper, TableInfo tableInfo) {
        return basicMapper.cursor(QueryUtil.buildNoOptimizationQuery(tableInfo, q -> QueryUtil.fillQueryDefault(q, tableInfo, null)));
    }
}
