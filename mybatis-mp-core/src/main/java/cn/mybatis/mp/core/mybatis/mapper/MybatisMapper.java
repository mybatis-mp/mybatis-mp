package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.GetterFun;
import db.sql.api.impl.cmd.executor.SelectorCall;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.LambdaUtil;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * 数据库 Mapper
 * $ 开头的方法一般不建议去使用
 *
 * @param <T>
 */
public interface MybatisMapper<T> extends CommonMapper {

    /**
     * 数据库适配器 不同数据库执行不同的方法
     *
     * @param consumer
     */
    default <R> R dbAdapt(Consumer<SelectorCall<R>> consumer) {
        return this.getBasicMapper().dbAdapt(consumer);
    }

    /**
     * 获取当前数据库的类型
     *
     * @return
     */
    default DbType getCurrentDbType() {
        return this.getBasicMapper().getCurrentDbType();
    }

    /**
     * 获取实体类的type
     *
     * @return 当前实体类
     */
    Class<T> getEntityType();

    BasicMapper getBasicMapper();

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 当个当前实体类
     */
    default T getById(Serializable id) {
        return getBasicMapper().getById(getEntityType(), id);
    }

    /**
     * 根据ID查询，只返回指定列
     *
     * @param id           ID
     * @param selectFields select列
     * @return 当个当前实体类
     */
    default T getById(Serializable id, Getter<T>... selectFields) {
        return getBasicMapper().getById(getEntityType(), id, selectFields);
    }


    /**
     * 根据实体类删除
     *
     * @param entity 实体类实例
     * @return 影响的数量
     */
    default int delete(T entity) {
        return getBasicMapper().delete(entity);
    }

    /**
     * 多个删除
     *
     * @param list 实体类实例list
     * @return 修改条数
     */
    default int delete(Collection<T> list) {
        return getBasicMapper().delete(list);
    }


    /**
     * 根据id删除
     *
     * @param id ID
     * @return 影响的数量
     */
    default int deleteById(Serializable id) {
        return getBasicMapper().deleteById(getEntityType(), id);
    }

    /**
     * 批量删除多个
     *
     * @param ids 多个ID
     * @return 影响的数量
     */
    default int deleteByIds(Serializable... ids) {
        return getBasicMapper().deleteByIds(getEntityType(), ids);
    }

    /**
     * 批量删除多个
     *
     * @param ids 多个ID
     * @return 影响数量
     */
    default <ID extends Serializable> int deleteByIds(Collection<ID> ids) {
        return getBasicMapper().deleteByIds(getEntityType(), ids);
    }

    /**
     * 动态条件删除
     *
     * @param consumer
     * @return
     */
    default int delete(Consumer<Where> consumer) {
        return getBasicMapper().delete(getEntityType(), consumer);
    }

    /**
     * 动态条件删除
     *
     * @param where
     * @return
     */
    default int delete(Where where) {
        return getBasicMapper().delete(getEntityType(), where);
    }

    /**
     * 删除所有数据
     *
     * @return
     */
    default int deleteAll() {
        return getBasicMapper().deleteAll(getEntityType());
    }


    /**
     * 单个查询
     *
     * @param consumer where consumer
     * @return 当个当前实体
     */
    default T get(Consumer<Where> consumer) {
        return getBasicMapper().get(getEntityType(), consumer);
    }

    default T getWithQueryFun(Consumer<BaseQuery<? extends BaseQuery, T>> consumer) {
        return getBasicMapper().getWithQueryFun(getEntityType(), consumer);
    }

    /**
     * 是否存在
     *
     * @param consumer where consumer
     * @return 是否存在
     */
    default boolean exists(Consumer<Where> consumer) {
        return getBasicMapper().exists(getEntityType(), consumer);
    }


    default boolean existsWithQueryFun(Consumer<BaseQuery<? extends BaseQuery, T>> consumer) {
        return getBasicMapper().existsWithQueryFun(getEntityType(), consumer);
    }


    /**
     * 实体类修改
     *
     * @param entity
     * @return 影响条数
     */
    default int update(T entity) {
        return getBasicMapper().update(entity);
    }

    /**
     * 实体类修改
     *
     * @param entity
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default int update(T entity, boolean allFieldForce) {
        return getBasicMapper().update(entity, allFieldForce);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 影响条数
     */
    default int update(Collection<T> list) {
        return getBasicMapper().update(list);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default int update(Collection<T> list, boolean allFieldForce) {
        return getBasicMapper().update(list, allFieldForce);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 修改条数
     */
    default int update(Collection<T> list, Getter<T>... forceUpdateFields) {
        return getBasicMapper().update(list, forceUpdateFields);
    }

    /**
     * 实体类修改
     *
     * @param entity
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 返回修改条数
     */
    default int update(T entity, Getter<T>... forceUpdateFields) {
        return getBasicMapper().update(entity, forceUpdateFields);
    }

    default int update(T entity, Consumer<Where> consumer) {
        return getBasicMapper().update(entity, consumer);
    }

    /**
     * @param entity        实体类
     * @param allFieldForce
     * @param consumer
     * @return
     */
    default int update(T entity, boolean allFieldForce, Consumer<Where> consumer) {
        return getBasicMapper().update(entity, allFieldForce, consumer);
    }

    /**
     * @param entity 实体类
     * @param where  可通过Wheres.create()创建
     * @return
     */
    default int update(T entity, Where where) {
        return getBasicMapper().update(entity, where, (Getter<T>[]) null);
    }

    /**
     * @param entity        实体类
     * @param where         可通过Wheres.create()创建
     * @param allFieldForce 强制更新指定，解决需要修改为null的需求
     * @return
     */
    default int update(T entity, boolean allFieldForce, Where where) {
        return getBasicMapper().update(entity, allFieldForce, where);
    }

    /**
     * @param entity            实体类
     * @param where             可通过Wheres.create()创建
     * @param forceUpdateFields 强制更新字段
     * @return
     */
    default int update(T entity, Where where, Getter<T>... forceUpdateFields) {
        return getBasicMapper().update(entity, where, forceUpdateFields);
    }

    /**
     * model修改 部分字段修改
     *
     * @param model 实体类model
     * @return 修改的条数
     */
    default <E> int update(Model<E> model) {
        return getBasicMapper().update(model);
    }

    /**
     * model修改 部分字段修改
     *
     * @param model         实体类model
     * @param allFieldForce 所有字段都强制保存
     * @return 修改的条数
     */
    default <E> int update(Model<E> model, boolean allFieldForce) {
        return getBasicMapper().update(model, allFieldForce);
    }

    /**
     * model修改 部分字段修改
     *
     * @param model             实体类model
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 修改的条数
     */
    default <E> int update(Model<E> model, Getter<E>... forceUpdateFields) {
        return getBasicMapper().update(model, forceUpdateFields);
    }

    default <E> int update(Model<E> model, Consumer<Where> consumer) {
        return getBasicMapper().update(model, consumer);
    }

    default <E> int update(Model<E> model, boolean allFieldForce, Consumer<Where> consumer) {
        return getBasicMapper().update(model, allFieldForce, consumer);
    }

    default <E> int update(Model<E> model, Where where, Getter<E>... forceUpdateFields) {
        return getBasicMapper().update(model, where, forceUpdateFields);
    }

    default <E> int update(Model<E> model, boolean allFieldForce, Where where) {
        return getBasicMapper().update(model, allFieldForce, where);
    }

    default <E> int update(Model<E> model, Where where, boolean allFieldForce, Getter<E>... forceUpdateFields) {
        return getBasicMapper().update(model, where, allFieldForce, forceUpdateFields);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 影响条数
     */
    default int updateModels(Collection<Model<T>> list) {
        return getBasicMapper().updateModels(list);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default int updateModels(Collection<Model<T>> list, boolean allFieldForce) {
        return getBasicMapper().updateModels(list, allFieldForce);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @param <E>
     * @return
     */
    default <E> int saveOrUpdate(E entity) {
        return getBasicMapper().saveOrUpdate(entity);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @param @allFieldForce 所有字段都强制保存
     * @param <E>
     * @return
     */
    default <E> int saveOrUpdate(E entity, boolean allFieldForce) {
        return getBasicMapper().saveOrUpdate(entity, allFieldForce);
    }


    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param <E>
     * @return
     */
    default <E> int saveOrUpdate(Model<E> model) {
        return getBasicMapper().saveOrUpdate(model);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param @allFieldForce 所有字段都强制保存
     * @param <E>
     * @return
     */
    default <E> int saveOrUpdate(Model<E> model, boolean allFieldForce) {
        return getBasicMapper().saveOrUpdate(model, allFieldForce);
    }


    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer where consumer
     * @return 返回结果列表
     */
    default List<T> list(Consumer<Where> consumer) {
        return getBasicMapper().list(getEntityType(), consumer);
    }

    default List<T> list(Where consumer, Getter<T>... selectFields) {
        return getBasicMapper().list(getEntityType(), consumer, selectFields);
    }

    default List<T> listWithQueryFun(Consumer<BaseQuery<? extends BaseQuery, T>> consumer) {
        return getBasicMapper().listWithQueryFun(getEntityType(), consumer);
    }

    /**
     * 游标查询,返回类型，当前实体类
     *
     * @param consumer where consumer
     * @return 返回游标
     */
    default Cursor<T> cursor(Consumer<Where> consumer) {
        return getBasicMapper().cursor(getEntityType(), consumer);
    }

    default Cursor<T> cursor(Where consumer, Getter<T>... selectFields) {
        return getBasicMapper().cursor(getEntityType(), consumer, selectFields);
    }

    default Cursor<T> cursorWithQueryFun(Consumer<BaseQuery<? extends BaseQuery, T>> consumer) {
        return getBasicMapper().cursorWithQueryFun(getEntityType(), consumer);
    }

    /**
     * count查询
     *
     * @param consumer where consumer
     * @return 返回count数
     */
    default Integer count(Consumer<Where> consumer) {
        return getBasicMapper().count(getEntityType(), consumer);
    }

    /**
     * count查询
     *
     * @param consumer query consumer
     * @return 返回count数
     */
    default Integer countWithQueryFun(Consumer<BaseQuery<? extends BaseQuery, T>> consumer) {
        return getBasicMapper().countWithQueryFun(getEntityType(), consumer);
    }

    /**
     * 分页查询
     *
     * @param consumer where consumer
     * @param pager    分页参数
     * @return 分页结果
     */
    default Pager<T> paging(Pager<T> pager, Consumer<Where> consumer) {
        return getBasicMapper().paging(getEntityType(), pager, consumer);
    }

    default Pager<T> paging(Consumer<Where> consumer, Pager<T> pager, Getter<T>... selectFields) {
        return getBasicMapper().paging(getEntityType(), consumer, pager, selectFields);
    }


    /**
     * 分页查询
     *
     * @param consumer query consumer
     * @return 返回page
     */
    default <P extends Pager<T>> P pagingWithQueryFun(P pager, Consumer<BaseQuery<? extends BaseQuery, T>> consumer) {
        return getBasicMapper().pagingWithQueryFun(getEntityType(), pager, consumer);
    }


    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Serializable... ids) {
        return getBasicMapper().mapWithKey(mapKey, ids);
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Collection<Serializable> ids) {
        return getBasicMapper().mapWithKey(mapKey, ids);
    }


    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey   map的key
     * @param consumer where consumer
     * @param <K>      map的key的类型
     * @return 一个map
     */
    default <K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Consumer<Where> consumer) {
        return getBasicMapper().mapWithKey(mapKey, consumer);
    }


    //通用

    /**
     * 动态查询 返回单个对象
     *
     * @param query 查询query
     * @param <E>   query设置的返回的类型
     * @return 单个对象
     */
    default <E, Q extends BaseQuery<Q, E>> E get(BaseQuery<Q, E> query) {
        return getBasicMapper().get(query);
    }

    /**
     * 是否存在
     *
     * @param query
     * @param <E>   query设置的返回的类型
     * @return 是否存在
     */
    default <E, Q extends BaseQuery<Q, E>> boolean exists(BaseQuery<Q, E> query) {
        return getBasicMapper().exists(query);
    }

    /**
     * 实体类新增
     *
     * @param entity
     * @return 影响条数
     */
    default <E> int save(E entity) {
        return this.save(entity, false);
    }

    /**
     * 实体类新增
     *
     * @param entity        实体类实例
     * @param allFieldForce 所有字段都强制保存
     * @return 影响条数
     */
    default <E> int save(E entity, boolean allFieldForce) {
        return this.$saveEntity(new EntityInsertContext(entity, allFieldForce));
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @return 插入条数
     */
    default <E> int save(List<E> list) {
        return this.save(list, false);
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param allFieldForce 所有字段都强制保存
     * @return 插入条数
     */
    default <E> int save(List<E> list, boolean allFieldForce) {
        int cnt = 0;
        for (Object entity : list) {
            cnt += this.save(entity, allFieldForce);
        }
        return cnt;
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     *
     * @param list
     * @return 插入的条数
     */
    default int saveBatch(Collection<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            return 0;
        }
        Set<String> saveFieldSet = new HashSet<>();
        final T first = list.stream().findFirst().get();
        TableInfo tableInfo = Tables.get(first.getClass());

        DbType dbType = getCurrentDbType();

        for (TableFieldInfo tableFieldInfo : tableInfo.getTableFieldInfos()) {
            if (!tableFieldInfo.getTableFieldAnnotation().insert()) {
                continue;
            }
            if (tableFieldInfo.isTableId()) {
                TableId tableId = TableInfoUtil.getTableIdAnnotation(tableFieldInfo.getField(), dbType);
                Objects.requireNonNull(tableId.value());
                if (tableId.value() == IdAutoType.AUTO) {
                    Object id;
                    try {
                        id = tableFieldInfo.getReadFieldInvoker().invoke(first, null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (Objects.isNull(id)) {
                        continue;
                    }
                }
            }
            saveFieldSet.add(tableFieldInfo.getField().getName());
        }
        return this.$save(new EntityBatchInsertContext(list, saveFieldSet));
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
    default int saveBatch(Collection<T> list, Getter<T>... saveFields) {
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
        return this.save(model, false);
    }

    /**
     * model插入 部分字段插入
     *
     * @param model
     * @param allFieldForce 所有字段都强制保存
     * @return
     */
    default <E> int save(Model<E> model, boolean allFieldForce) {
        return this.$saveModel(new ModelInsertContext<>(model, allFieldForce));
    }

    /**
     * 批量保存
     *
     * @param list
     * @param <E>
     * @return
     */
    default <E> int saveModels(List<Model<E>> list) {
        return this.saveModels(list, false);
    }

    /**
     * 多个保存，非批量操作
     *
     * @param list
     * @param allFieldForce 所有字段都强制保存
     * @param <E>
     * @return
     */
    default <E> int saveModels(List<Model<E>> list, boolean allFieldForce) {
        int cnt = 0;
        for (Model<E> entity : list) {
            cnt += this.save(entity, allFieldForce);
        }
        return cnt;
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
        return this.getBasicMapper().update(update);
    }

    /**
     * 动态删除
     *
     * @param delete 上下文
     * @return 删除条数
     */
    default int delete(BaseDelete<?> delete) {
        return this.getBasicMapper().delete(delete);
    }

    /**
     * 列表查询
     *
     * @param query 查询query
     * @return 返回结果列表
     */
    default <E, Q extends BaseQuery<Q, E>> List<E> list(BaseQuery<Q, E> query) {
        return getBasicMapper().list(query);
    }

    /**
     * 游标查询
     *
     * @param query 查询query
     * @return 返回游标
     */
    default <E, Q extends BaseQuery<Q, E>> Cursor<E> cursor(BaseQuery<Q, E> query) {
        return getBasicMapper().cursor(query);
    }

    /**
     * count查询
     *
     * @param query 上下文
     * @return 返回count 数
     */
    default <E, Q extends BaseQuery<Q, E>> Integer count(BaseQuery<Q, E> query) {
        return getBasicMapper().count(query);
    }

    /**
     * 分页查询
     *
     * @param query 查询query
     * @param pager 分页参数
     * @return 分页结果
     */
    default <E, Q extends BaseQuery<Q, E>, P extends Pager<E>> P paging(BaseQuery<Q, E> query, P pager) {
        return getBasicMapper().paging(query, pager);
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
    default <K, V, Q extends BaseQuery<Q, V>> Map<K, V> mapWithKey(GetterFun<V, K> mapKey, BaseQuery<Q, V> query) {
        return getBasicMapper().mapWithKey(mapKey, query);
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
    default <K, V, Q extends BaseQuery<Q, V>> Map<K, V> mapWithKey(String mapKey, BaseQuery<Q, V> query) {
        return getBasicMapper().mapWithKey(mapKey, query);
    }
}
