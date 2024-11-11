package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.Objects;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class ListMethodUtil {

    private static <T> List<T> listByIds(BasicMapper basicMapper, TableInfo tableInfo, Getter<T>[] selectFields, Consumer<Where> whereConsumer) {
        return list(basicMapper, tableInfo, null, WhereUtil.create(tableInfo, whereConsumer), selectFields);
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

    public static <T> List<T> list(BasicMapper basicMapper, TableInfo tableInfo, Integer limit, Consumer<Where> consumer) {
        return list(basicMapper, tableInfo, limit, consumer, null);
    }

    public static <T> List<T> list(BasicMapper basicMapper, TableInfo tableInfo, Integer limit, Consumer<Where> consumer, Getter<T>[] selectFields) {
        return list(basicMapper, tableInfo, limit, WhereUtil.create(tableInfo, consumer), selectFields);
    }

    public static <T> List<T> list(BasicMapper basicMapper, TableInfo tableInfo, Integer limit, Where where, Getter<T>[] selectFields) {
        return basicMapper.list(QueryUtil.buildNoOptimizationQuery(tableInfo, where, query -> {
            QueryUtil.fillQueryDefault(query, tableInfo, selectFields);
            if (Objects.nonNull(limit)) {
                query.limit(2);
                query.dbAdapt(((q, selector) -> {
                    selector.when(DbType.SQL_SERVER, () -> {
                        if (Objects.isNull(q.getOrderBy())) {
                            tableInfo.getIdFieldInfos().stream().forEach(item -> {
                                q.orderBy(q.$(tableInfo.getType(), item.getField().getName()));
                            });
                        }
                    });
                }));
            }
        }));
    }

    public static <T> List<T> listAll(BasicMapper basicMapper, TableInfo tableInfo) {
        return basicMapper.list(QueryUtil.buildNoOptimizationQuery(tableInfo, q -> QueryUtil.fillQueryDefault(q, tableInfo, null)));
    }
}
