package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class ListMethodUtil {

    private static <T> List<T> listByIds(BasicMapper basicMapper, TableInfo tableInfo, Getter<T>[] selectFields, Consumer<Where> whereConsumer) {
        return list(basicMapper, tableInfo, WhereUtil.create(tableInfo, whereConsumer), selectFields);
    }

    public static <T> List<T> listByIds(BasicMapper basicMapper, TableInfo tableInfo, Serializable[] ids, Getter<T>[] selectFields) {
        return listByIds(basicMapper, tableInfo, selectFields, where -> {
            WhereUtil.appendIdsWhere(where, tableInfo, ids);
        });
    }

    public static <T, ID extends Serializable> List<T> listByIds(BasicMapper basicMapper, TableInfo tableInfo, Collection<ID> ids, Getter<T>[] selectFields) {
        return listByIds(basicMapper, tableInfo, selectFields, where -> {
            WhereUtil.appendIdsWhere(where, tableInfo, ids);
        });
    }

    public static <T> List<T> list(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer) {
        return list(basicMapper, tableInfo, consumer, null);
    }

    public static <T> List<T> list(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer, Getter<T>[] selectFields) {
        return list(basicMapper, tableInfo, WhereUtil.create(tableInfo, consumer), selectFields);
    }

    public static <T> List<T> list(BasicMapper basicMapper, TableInfo tableInfo, Where where, Getter<T>[] selectFields) {
        return basicMapper.list(QueryUtil.buildNoOptimizationQuery(tableInfo, where, q -> QueryUtil.fillQueryDefault(q, tableInfo, selectFields)));
    }

    public static <T> List<T> listAll(BasicMapper basicMapper, TableInfo tableInfo) {
        return basicMapper.list(QueryUtil.buildNoOptimizationQuery(tableInfo, q -> QueryUtil.fillQueryDefault(q, tableInfo, null)));
    }
}
