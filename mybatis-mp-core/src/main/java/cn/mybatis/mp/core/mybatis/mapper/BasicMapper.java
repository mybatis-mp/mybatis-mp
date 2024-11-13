package cn.mybatis.mp.core.mybatis.mapper;


import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper.*;
import cn.mybatis.mp.core.mybatis.provider.TablePrefixUtil;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.executor.SelectorCall;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import java.util.*;
import java.util.function.Consumer;

public interface BasicMapper extends CRUDMapper, GetBasicMapper, ExistsBasicMapper, CountBasicMapper, ListBasicMapper, CursorBasicMapper,
        PagingBasicMapper, MapWithKeyBasicMapper, SaveBasicMapper, SaveOrUpdateBasicMapper, SaveModelBasicMapper, SaveOrUpdateModelBasicMapper,
        UpdateBasicMapper, UpdateModelBasicMapper, DeleteBasicMapper {

    /**
     * 获取当前数据库的类型
     *
     * @return DbType
     */
    DbType getCurrentDbType();

    /**
     * 选择器 不同数据库执行不同的方法
     *
     * @param consumer
     */
    <R> R dbAdapt(Consumer<SelectorCall<R>> consumer);

    /**
     * 获取基础Mapper
     *
     * @return BasicMapper
     */
    default BasicMapper getBasicMapper() {
        return this;
    }

    @Override
    default <T> T get(BaseQuery<? extends BaseQuery, T> query) {
        return this.$get(new SQLCmdQueryContext(query), new RowBounds(0, 2));
    }

    @Override
    default boolean exists(BaseQuery<? extends BaseQuery, ?> query) {
        if (Objects.isNull(query.getSelect())) {
            query.select1();
        }
        query.dbAdapt((q, selector) -> {
            selector.when(DbType.SQL_SERVER, () -> {
                query.getSelect().top(1);
                query.removeLimit();
            }).otherwise(() -> {
                query.limit(1);
            });
        });

        query.setReturnType(Integer.class);
        Integer obj = (Integer) this.get(query);
        return Objects.nonNull(obj) && obj >= 1;
    }

    @Override
    default int save(BaseInsert<?> insert) {
        return this.$save(new SQLCmdInsertContext<>(insert));
    }

    @Override
    default int update(BaseUpdate<?> update) {
        return this.$update(new SQLCmdUpdateContext(update));
    }

    @Override
    default int delete(BaseDelete<?> delete) {
        return this.$delete(new SQLCmdDeleteContext(delete));
    }

    @Override
    default <T> List<T> list(BaseQuery<? extends BaseQuery, T> query) {
        return this.$list(new SQLCmdQueryContext(query));
    }

    @Override
    default <T> Cursor<T> cursor(BaseQuery<? extends BaseQuery, T> query) {
        return this.$cursor(new SQLCmdQueryContext(query));
    }

    @Override
    default Integer count(BaseQuery<? extends BaseQuery, ?> query) {
        query.setReturnType(Integer.class);
        return this.$count(new SQLCmdCountQueryContext(query));
    }

    @Override
    default <T, P extends Pager<T>> P paging(BaseQuery<? extends BaseQuery, T> query, P pager) {
        if (pager.isExecuteCount()) {
            Class returnType = query.getReturnType();
            TablePrefixUtil.prefixMapping(query, returnType);
            query.setReturnType(Integer.class);
            Integer count = this.$countFromQuery(new SQLCmdCountFromQueryContext(query));
            query.setReturnType(returnType);

            pager.setTotal(Optional.of(count).orElse(0));
            if (pager.getTotal() < 1) {
                pager.setResults(Collections.emptyList());
                return pager;
            }
        }
        query.limit(pager.getOffset(), pager.getSize());
        pager.setResults(this.list(query));
        return pager;
    }

    @Override
    default <K, V> Map<K, V> mapWithKey(String mapKey, BaseQuery<? extends BaseQuery, V> query) {
        return this.$mapWithKey(new MapKeySQLCmdQueryContext(mapKey, query));
    }
}
