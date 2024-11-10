package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.QueryUtil;
import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdQueryContext;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.function.Consumer;

public final class GetMethodUtil {

    public static <T> T getById(BasicMapper basicMapper, TableInfo tableInfo, Serializable id, Getter<T>[] selectFields) {
        Where where = WhereUtil.create(tableInfo, w -> WhereUtil.appendIdWhere(w, tableInfo, id));
        BaseQuery<?, T> query = QueryUtil.buildNoOptimizationQuery(tableInfo, where, q -> QueryUtil.fillQueryDefault(q, tableInfo, selectFields));
        return basicMapper.$getById(new SQLCmdQueryContext(query), new RowBounds(0, 1));
    }

    public static <T> T get(BasicMapper basicMapper, TableInfo tableInfo, Where where, Getter<T>[] selectFields) {
        return basicMapper.get(QueryUtil.buildNoOptimizationQuery(tableInfo, where, q -> QueryUtil.fillQueryDefault(q, tableInfo, selectFields)));
    }

    public static <T> T get(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer) {
        return get(basicMapper, tableInfo, WhereUtil.create(tableInfo, where -> consumer.accept(where)), null);
    }

    public static <T> T get(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer, Getter<T>[] selectFields) {
        return basicMapper.get(QueryUtil.buildNoOptimizationQuery(tableInfo, WhereUtil.create(tableInfo, consumer),
                q -> QueryUtil.fillQueryDefault(q, tableInfo, selectFields)));
    }

}
