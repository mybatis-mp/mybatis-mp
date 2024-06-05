package cn.mybatis.mp.core.mybatis.mapper;


import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.util.WhereUtil;

import java.util.Objects;
import java.util.function.Consumer;

public final class MapperCmdBuilderUtil {

    public static void setDefault(Class<?> entityType, BaseQuery<? extends BaseQuery, ?> query) {
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

    public static <E> BaseQuery<? extends BaseQuery, E> buildQuery(Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return buildQuery(WhereUtil.create(), consumer);
    }

    public static <E> BaseQuery<? extends BaseQuery, E> buildQuery(db.sql.api.impl.cmd.struct.Where where, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        BaseQuery<? extends BaseQuery, E> query = (BaseQuery<? extends BaseQuery, E>) Query.create(where);
        consumer.accept(query);
        return query;
    }

    public static <E> BaseQuery<? extends BaseQuery, E> buildQuery(Class<E> entityType, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return buildQuery(entityType, WhereUtil.create(), consumer);
    }

    public static <E> BaseQuery<? extends BaseQuery, E> buildQuery(Class<E> entityType, db.sql.api.impl.cmd.struct.Where where, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        BaseQuery<? extends BaseQuery, E> query = buildQuery(where, consumer);
        setDefault(entityType, query);
        return query;
    }

    public static <E> BaseQuery<? extends BaseQuery, E> buildQuery(Class<E> entityType, db.sql.api.impl.cmd.struct.Where where) {
        BaseQuery<? extends BaseQuery, E> query = (BaseQuery<?, E>) Query.create(where);
        setDefault(entityType, query);
        return query;
    }
}
