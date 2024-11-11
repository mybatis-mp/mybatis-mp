package cn.mybatis.mp.core.mybatis.mapper;


import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public final class QueryUtil {

    public static <T, Q extends BaseQuery<Q, T>> void fillQueryDefault(Q q, TableInfo tableInfo) {
        fillQueryDefault(q, tableInfo, null);
    }

    public static <T, Q extends BaseQuery<Q, T>> void fillQueryDefault(Q q, TableInfo tableInfo, Getter<T>[] selectFields) {
        if (Objects.nonNull(selectFields) && selectFields.length > 0) {
            q.select(selectFields);
        } else {
            if (Objects.isNull(q.getSelect())) {
                if (tableInfo.isHasIgnoreField()) {
                    q.select(tableInfo.getType());
                } else {
                    q.select(q.$().table(tableInfo.getType(), 1).$("*"));
                }
            }
        }
        if (Objects.isNull(q.getFrom())) {
            q.from(tableInfo.getType());
        }
        if (Objects.isNull(q.getReturnType())) {
            q.setReturnType(tableInfo.getType());
        }
    }


    public static <T, Q extends BaseQuery<Q, T>> Q buildQuery(Consumer<Q> consumer) {
        return buildQuery(WhereUtil.create(), consumer);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildQuery(TableInfo tableInfo) {
        return (Q) Query.create().from(tableInfo.getType());
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildQuery(Where where, Consumer<Q> consumer) {
        Q query = (Q) Query.create(where);
        if (Objects.nonNull(consumer)) {
            consumer.accept(query);
        }
        return query;
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildQuery(TableInfo tableInfo, Consumer<Q> consumer) {
        return buildQuery(tableInfo, WhereUtil.create(tableInfo), consumer);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildQuery(TableInfo tableInfo, Where where) {
        return buildQuery(tableInfo, where, null);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildQuery(TableInfo tableInfo, Where where, Consumer<Q> consumer) {
        Q query = buildQuery(where, consumer);
        query.$().cacheTableInfo(tableInfo);
        return query;
    }

    public static <ID extends Serializable, E> BaseQuery<? extends BaseQuery, E> buildIdsQuery(TableInfo tableInfo, Collection<ID> ids) {
        return buildIdsQuery(tableInfo, ids, null);
    }

    public static <ID extends Serializable, T, Q extends BaseQuery<Q, T>> Q buildIdsQuery(TableInfo tableInfo, Collection<ID> ids, Consumer<Q> consumer) {
        Where where = WhereUtil.create(tableInfo);
        WhereUtil.appendIdsWhere(where, tableInfo, ids);
        return buildNoOptimizationQuery(tableInfo, where, consumer);
    }

    public static <ID extends Serializable, E> BaseQuery<? extends BaseQuery, E> buildIdsQuery(TableInfo tableInfo, ID[] ids) {
        return buildIdsQuery(tableInfo, ids, null);
    }

    public static <ID extends Serializable, T, Q extends BaseQuery<Q, T>> Q buildIdsQuery(TableInfo tableInfo, ID[] ids, Consumer<Q> consumer) {
        Where where = WhereUtil.create(tableInfo);
        WhereUtil.appendIdsWhere(where, tableInfo, ids);
        return buildNoOptimizationQuery(tableInfo, where, consumer);
    }

    public static <ID extends Serializable, E> BaseQuery<? extends BaseQuery, E> buildIdQuery(TableInfo tableInfo, ID id) {
        return buildIdQuery(tableInfo, id, null);
    }

    public static <ID extends Serializable, T, Q extends BaseQuery<Q, T>> Q buildIdQuery(TableInfo tableInfo, ID id, Consumer<Q> consumer) {
        Where where = WhereUtil.create(tableInfo);
        WhereUtil.appendIdWhere(where, tableInfo, id);
        return buildNoOptimizationQuery(tableInfo, where, consumer);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildNoOptimizationQuery(TableInfo tableInfo) {
        return buildNoOptimizationQuery(tableInfo, null, null);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildNoOptimizationQuery(TableInfo tableInfo, Consumer<Q> consumer) {
        return buildNoOptimizationQuery(tableInfo, null, consumer);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildNoOptimizationQuery(TableInfo tableInfo, Where where) {
        return buildNoOptimizationQuery(tableInfo, where, null);
    }

    public static <T, Q extends BaseQuery<Q, T>> Q buildNoOptimizationQuery(TableInfo tableInfo, Where where, Consumer<Q> consumer) {
        Q query = buildQuery(tableInfo, where, consumer);
        query.optimizeOptions(optimizeOptions -> optimizeOptions.disableAll());
        return query;
    }

}
