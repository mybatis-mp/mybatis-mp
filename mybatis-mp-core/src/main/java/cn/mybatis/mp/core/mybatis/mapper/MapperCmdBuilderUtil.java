package cn.mybatis.mp.core.mybatis.mapper;


import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.util.WhereUtil;

import java.util.Objects;
import java.util.function.Consumer;

public final class MapperCmdBuilderUtil {

    public static void setDefault(Class<?> entityType, BaseQuery<?, ?> query) {
        if (Objects.isNull(query.getSelect()) || query.getSelect().getSelectField().isEmpty()) {
            TableInfo tableInfo = Tables.get(entityType);
            if (tableInfo.isHasIgnoreField()) {
                query.select(entityType);
            } else {
                query.select(query.$().table(entityType, 1).$("*"));
            }
        }
        if (Objects.isNull(query.getFrom()) || query.getFrom().getTables().isEmpty()) {
            query.from(entityType);
        }
        if (Objects.isNull(query.getReturnType())) {
            query.setReturnType(entityType);
        }
    }

    public static <E, Q extends BaseQuery<Q, E>> BaseQuery<Q, E> buildQuery(Consumer<BaseQuery<Q, E>> consumer) {
        return (BaseQuery<Q, E>) buildQuery(WhereUtil.create(), consumer);
    }

    public static <E, Q extends BaseQuery<Q, E>> BaseQuery<Q, E> buildQuery(db.sql.api.impl.cmd.struct.Where where, Consumer<BaseQuery<Q, E>> consumer) {
        BaseQuery<Q, E> query = (BaseQuery<Q, E>) Query.create(where);
        consumer.accept(query);
        return query;
    }

    public static <E, Q extends BaseQuery<Q, E>> BaseQuery<Q, E> buildQuery(Class<E> entityType, Consumer<BaseQuery<Q, E>> consumer) {
        return buildQuery(entityType, WhereUtil.create(), consumer);
    }

    public static <E, Q extends BaseQuery<Q, E>> BaseQuery<Q, E> buildQuery(Class<E> entityType, db.sql.api.impl.cmd.struct.Where where, Consumer<BaseQuery<Q, E>> consumer) {
        BaseQuery<Q, E> query = buildQuery(where, consumer);
        setDefault(entityType, query);
        return query;
    }

    public static <E, Q extends BaseQuery<Q, E>> BaseQuery<Q, E> buildQuery(Class<E> entityType, db.sql.api.impl.cmd.struct.Where where) {
        BaseQuery<Q, E> query = (BaseQuery<Q, E>) Query.create(where);
        setDefault(entityType, query);
        return query;
    }
}
