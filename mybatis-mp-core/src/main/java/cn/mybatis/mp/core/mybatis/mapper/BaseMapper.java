package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.mybatis.configuration.MapKeySQLCmdQueryContext;
import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.mybatis.provider.MybatisSQLProvider;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import cn.mybatis.mp.db.Model;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.GetterFun;
import db.sql.api.impl.tookit.LambdaUtil;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import java.util.*;

public interface BaseMapper extends CommonMapper {


    /**
     * 动态查询 返回单个当前实体
     *
     * @param query 查询query
     * @param <E>   返回类
     * @return 单个当前实体
     */
    default <E> E get(BaseQuery<? extends BaseQuery, E> query) {
        return this.get(query, true);
    }

    /**
     * 动态查询
     *
     * @param query    查询query
     * @param optimize 是否优化
     * @param <E>      返回类
     * @return 返回单个对象
     */
    default <E> E get(BaseQuery<? extends BaseQuery, E> query, boolean optimize) {
        return this.$get(new SQLCmdQueryContext(query, optimize), new RowBounds(0, 2));
    }

    /**
     * 是否存在
     *
     * @param query
     * @param <E>   返回类
     * @return 是否存在
     */
    default <E> boolean exists(BaseQuery<? extends BaseQuery, E> query) {
        return this.exists(query, true);
    }

    /**
     * 是否存在
     *
     * @param query    子查询
     * @param optimize 是否优化
     * @param <E>      返回类
     * @return 是否存在
     */
    default <E> boolean exists(BaseQuery<? extends BaseQuery, E> query, boolean optimize) {
        if (Objects.isNull(query.getSelect())) {
            query.select1();
        }
        query.limit(1);
        query.setReturnType(Integer.TYPE);
        Integer obj = (Integer) this.get(query, optimize);
        return Objects.nonNull(obj) && obj >= 1;
    }

    /**
     * 实体类新增
     *
     * @param entity
     * @return 影响条数
     */
    default <E> int save(E entity) {
        return this.$saveEntity(new EntityInsertContext(entity));
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @return 插入条数
     */
    default <E> int save(List<E> list) {
        int cnt = 0;
        for (Object entity : list) {
            cnt += this.save(entity);
        }
        return cnt;
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     * <p>
     * 会自动加入 主键 租户ID 逻辑删除列 乐观锁
     * 自动设置 默认值,不会忽略NULL值字段
     *
     * @param list
     * @param saveFields 指定那些列插入
     * @return 插入的条数
     */
    default <E> int saveBatch(List<E> list, Getter<E>... saveFields) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            return 0;
        }
        if (Objects.isNull(saveFields) || saveFields.length < 1) {
            throw new RuntimeException("saveFields can't be null or empty");
        }
        Set<String> saveFieldSet = new HashSet<>();
        for (Getter<?> column : saveFields) {
            saveFieldSet.add(LambdaUtil.getName(column));
        }
        return this.$save(new EntityBatchInsertContext(list, saveFieldSet));
    }

    /**
     * model插入 部分字段插入
     *
     * @param model
     * @return
     */
    default <E> int save(Model<E> model) {
        return this.$saveModel(new ModelInsertContext<>(model));
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
     * @return 返回结果列表
     */
    default <E> List<E> list(BaseQuery<? extends BaseQuery, E> query) {
        return this.list(query, true);
    }


    /**
     * 列表查询
     *
     * @param query    查询query
     * @param optimize 是否优化
     * @return 返回查询列表
     */
    default <E> List<E> list(BaseQuery<? extends BaseQuery, E> query, boolean optimize) {
        return this.$list(new SQLCmdQueryContext(query, optimize));
    }

    /**
     * 游标查询
     *
     * @param query 查询query
     * @return 返回游标
     */
    default <E> Cursor<E> cursor(BaseQuery<? extends BaseQuery, E> query) {
        return this.cursor(query, true);
    }

    /**
     * 游标查询
     *
     * @param query    查询query
     * @param optimize 是否优化
     * @return 返回游标
     */
    default <E> Cursor<E> cursor(BaseQuery<? extends BaseQuery, E> query, boolean optimize) {
        return this.$cursor(new SQLCmdQueryContext(query, optimize));
    }

    /**
     * count查询
     *
     * @param query 上下文
     * @return 返回count 数
     */
    default Integer count(BaseQuery<? extends BaseQuery, ?> query) {
        return this.count(query, false);
    }

    /**
     * count查询
     *
     * @param query    上下文
     * @param optimize 是否优化
     * @return 返回count 数
     */
    default Integer count(BaseQuery<? extends BaseQuery, ?> query, boolean optimize) {
        query.setReturnType(Integer.TYPE);
        return this.$count(new SQLCmdCountQueryContext(query, optimize));
    }


    /**
     * 分页查询
     *
     * @param query 查询query
     * @param pager 分页参数
     * @return 分页结果
     */
    default <E, P extends Pager<E>> P paging(BaseQuery<? extends BaseQuery, E> query, P pager) {
        if (pager.isExecuteCount()) {
            Class returnType = query.getReturnType();
            query.setReturnType(Integer.TYPE);
            Integer count = this.$countFromQuery(new SQLCmdCountFromQueryContext(query, pager.isOptimize()));
            query.setReturnType(returnType);

            pager.setTotal(Optional.of(count).orElse(0));
            if (pager.getTotal() < 1) {
                pager.setResults(Collections.emptyList());
                return pager;
            }
        }
        query.limit(pager.getOffset(), pager.getSize());
        pager.setResults(this.list(query, pager.isOptimize()));
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
    default <K, V> Map<K, V> mapWithKey(GetterFun<V, K> mapKey, BaseQuery<? extends BaseQuery, V> query) {
        return this.mapWithKey(mapKey, query, true);
    }

    /**
     * 将结果转成map
     *
     * @param mapKey   指定的map的key属性
     * @param query    查询对象
     * @param optimize 是否优化sql
     * @param <K>      map的key
     * @param <V>      map的value
     * @return
     */
    default <K, V> Map<K, V> mapWithKey(GetterFun<V, K> mapKey, BaseQuery<? extends BaseQuery, V> query, boolean optimize) {
        return this.mapWithKey(LambdaUtil.getName(mapKey), query, optimize);
    }

    /**
     * 将结果转成map
     *
     * @param mapKey   指定的map的key属性
     * @param query    查询对象
     * @param optimize 是否优化sql
     * @param <K>      map的key
     * @param <V>      map的value
     * @return
     */
    default <K, V> Map<K, V> mapWithKey(String mapKey, BaseQuery<? extends BaseQuery, V> query, boolean optimize) {
        return this.$mapWithKey(new MapKeySQLCmdQueryContext(mapKey, query, optimize));
    }

    /**
     * 动态查询 返回单个
     *
     * @param queryContext 上下文
     * @return 返回单个查询
     * @see MybatisSQLProvider#cmdQuery(SQLCmdQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.GET_QUERY_NAME)
    <R> R $get(SQLCmdQueryContext queryContext, RowBounds rowBounds);


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
    <E> List<E> $list(SQLCmdQueryContext queryContext);

    /**
     * 游标查询
     *
     * @param queryContext 上下文
     * @return 返回游标
     * @see MybatisSQLProvider#cmdQuery(SQLCmdQueryContext, ProviderContext, DbType)
     */
    @SelectProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.QUERY_NAME)
    <E> Cursor<E> $cursor(SQLCmdQueryContext queryContext);

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
