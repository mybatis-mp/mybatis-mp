package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.util.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.Objects;

import java.util.function.Consumer;

public final class PagingMethodUtil {

    public static <T, P extends Pager<T>> P paging(BasicMapper basicMapper, TableInfo tableInfo, P pager, Consumer<Where> consumer) {
        return paging(basicMapper, tableInfo, pager, consumer, null);
    }

    public static <T, P extends Pager<T>> P paging(BasicMapper basicMapper, TableInfo tableInfo, P pager, Consumer<Where> consumer, Getter<T>[] selectFields) {
        return paging(basicMapper, tableInfo, pager, WhereUtil.create(tableInfo, consumer), selectFields);
    }

    public static <T, P extends Pager<T>> P paging(BasicMapper basicMapper, TableInfo tableInfo, P pager, Where where) {
        return paging(basicMapper, tableInfo, pager, where, null);
    }

    public static <T, P extends Pager<T>> P paging(BasicMapper basicMapper, TableInfo tableInfo, P pager, Where where, Getter<T>[] selectFields) {
        return basicMapper.paging(QueryUtil.buildQuery(tableInfo, where, query -> {
            QueryUtil.fillQueryDefault(query, tableInfo, selectFields);
            query.dbAdapt(((q, selector) -> {
                selector.when(DbType.SQL_SERVER, () -> {
                    if (Objects.isNull(q.getOrderBy())) {
                        if (tableInfo.getIdFieldInfos().isEmpty()) {
                            //没有主键 取第一个
                            q.orderBy(q.$(tableInfo.getType(), tableInfo.getTableFieldInfos().get(0).getField().getName()));
                        } else {
                            tableInfo.getIdFieldInfos().forEach(item -> q.orderBy(q.$(tableInfo.getType(), item.getField().getName())));
                        }
                    }
                }).otherwise();
            }));
        }), pager);
    }
}
