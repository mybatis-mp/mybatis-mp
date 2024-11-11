package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.configuration.MapKeySQLCmdQueryContext;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.mybatis.provider.MybatisSQLProvider;
import cn.mybatis.mp.core.mybatis.provider.TablePrefixUtil;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.executor.SelectorCall;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import java.util.*;
import java.util.function.Consumer;

public interface BaseMapper {

    /**
     * 获取当前数据库的类型
     *
     * @return
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
    BasicMapper getBasicMapper();

    /**
     * 动态查询
     *
     * @param query 查询query
     * @param <T>   返回类
     * @return 返回单个对象
     */
    default <T> T get(BaseQuery<? extends BaseQuery, T> query) {
        return this.$get(new SQLCmdQueryContext(query), new RowBounds(0, 2));
    }

    /**
     * 是否存在
     *
     * @param query 子查询
     * @param <T>   返回类
     * @return 是否存在
     */
    default <T> boolean exists(BaseQuery<? extends BaseQuery, T> query) {
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

    /**
     * 动态插入
     *
     * @param insert
     * @return 影响条数
     */
    default int save(BaseInsert<?> insert) {
        return this.$save(new SQLCmdInsertContext<>(insert));
    }

    /**
     * 动态修改
     *
     * @param update 修改update
     * @return 修改的条数
     */
    default int update(BaseUpdate<?> update) {
        return this.$update(new SQLCmdUpdateContext(update));
    }

    /**
     * 动态删除
     *
     * @param delete 上下文
     * @return 删除条数
     */
    default int delete(BaseDelete<?> delete) {
        return this.$delete(new SQLCmdDeleteContext(delete));
    }


    /**
     * 列表查询
     *
     * @param query 查询query
     * @return 返回查询列表
     */
    default <T> List<T> list(BaseQuery<? extends BaseQuery, T> query) {
        return this.$list(new SQLCmdQueryContext(query));
    }

    /**
     * 游标查询
     *
     * @param query 查询query
     * @return 返回游标
     */
    default <T> Cursor<T> cursor(BaseQuery<? extends BaseQuery, T> query) {
        return this.$cursor(new SQLCmdQueryContext(query));
    }


    /**
     * count查询
     *
     * @param query 上下文
     * @return 返回count 数
     */
    default Integer count(BaseQuery<? extends BaseQuery, ?> query) {
        query.setReturnType(Integer.class);
        return this.$count(new SQLCmdCountQueryContext(query));
    }


    /**
     * 分页查询
     *
     * @param query 查询query
     * @param pager 分页参数
     * @return 分页结果
     */
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

    /**
     * 将结果转成map
     *
     * @param mapKey 指定的map的key属性
     * @param query  查询对象
     * @param <K>    map的key
     * @param <V>    map的value
     * @return
     */
    default <K, V> Map<K, V> mapWithKey(String mapKey, BaseQuery<? extends BaseQuery, V> query) {
        return this.$mapWithKey(new MapKeySQLCmdQueryContext(mapKey, query));
    }

    /**
     * 动态查询 返回单个
     *
     * @param queryContext 上下文
     * @return 返回单个查询
     * @see MybatisSQLProvider#getCmdQuery (SQLCmdQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.GET_QUERY_NAME)
    <R> R $get(SQLCmdQueryContext queryContext, RowBounds rowBounds);

    /**
     * ID查询 返回单个
     *
     * @param queryContext 上下文
     * @return 返回单个查询
     * @see MybatisSQLProvider#getByIdCmdQuery (SQLCmdQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.GET_BY_ID_QUERY_NAME)
    <R> R $getById(SQLCmdQueryContext queryContext, RowBounds rowBounds);

    /**
     * @param insertContext 上下文
     * @return 插入的条数
     * @see MybatisSQLProvider#save(SQLCmdInsertContext, ProviderContext, DbType) (SQLCmdInsertContext, ProviderContext)
     */
    @InsertProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.SAVE_NAME)
    int $save(SQLCmdInsertContext insertContext);


    /**
     * @param insertContext 上下文
     * @return 返回插入的条数
     * @see MybatisSQLProvider#save(SQLCmdInsertContext, ProviderContext, DbType) (SQLCmdInsertContext, ProviderContext)
     */
    @InsertProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.SAVE_NAME)
    int $saveEntity(EntityInsertContext insertContext);


    /**
     * @param insertContext 上下文
     * @return 插入的条数
     * @see MybatisSQLProvider#save(SQLCmdInsertContext, ProviderContext, DbType) (SQLCmdInsertContext, ProviderContext)
     */
    @InsertProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.SAVE_NAME)
    int $saveModel(ModelInsertContext insertContext);

    /**
     * @param updateContext 上下文
     * @return 修改的条数
     * @see MybatisSQLProvider#update(SQLCmdUpdateContext, ProviderContext, DbType)
     */
    @UpdateProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.UPDATE_NAME)
    int $update(SQLCmdUpdateContext updateContext);

    /**
     * @param deleteContext 上下文
     * @return 删除的条数
     * @see MybatisSQLProvider#delete(SQLCmdDeleteContext, ProviderContext, DbType)
     */
    @UpdateProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.DELETE_NAME)
    int $delete(SQLCmdDeleteContext deleteContext);

    /**
     * 列表查询
     *
     * @param queryContext 上下文
     * @return 返回查询的结果
     * @see MybatisSQLProvider#cmdCount(SQLCmdCountQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.QUERY_NAME)
    <T> List<T> $list(SQLCmdQueryContext queryContext);

    /**
     * 游标查询
     *
     * @param queryContext 上下文
     * @return 返回游标
     * @see MybatisSQLProvider#cmdQuery(SQLCmdQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.QUERY_NAME)
    <T> Cursor<T> $cursor(SQLCmdQueryContext queryContext);

    /**
     * count查询
     *
     * @param queryContext 上下文
     * @return 返回count数
     * @see MybatisSQLProvider#cmdCount(SQLCmdCountQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.COUNT_NAME)
    Integer $count(SQLCmdCountQueryContext queryContext);

    /**
     * count查询 - 从query中
     *
     * @param queryContext 上下文
     * @return 返回count数
     * @see MybatisSQLProvider#countFromQuery(SQLCmdCountFromQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.QUERY_COUNT_NAME)
    Integer $countFromQuery(SQLCmdCountFromQueryContext queryContext);

    /**
     * 将结果转成map
     *
     * @param queryContext 查询上下文
     * @param <K>          指定返回map的key的属性
     * @param <V>          指定返回map的value的类型
     * @return map
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.QUERY_NAME)
    <K, V> Map<K, V> $mapWithKey(MapKeySQLCmdQueryContext queryContext);
}
