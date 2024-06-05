package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.Delete;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.core.util.WhereUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.GetterFun;
import db.sql.api.cmd.executor.DBRunnable;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.LambdaUtil;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public interface BasicMapper extends BaseMapper {

    void runDBRunner(Consumer<DBRunnable<BasicMapper, BasicMapper>> consumer);

    /**
     * 根据ID查询，只返回指定列
     *
     * @param entityType 实体类
     * @param id         ID
     * @return 当个当前实体类
     */
    default <E> E getById(Class<E> entityType, Serializable id) {
        return this.getById(entityType, id, (Getter<E>[]) null);
    }

    /**
     * 根据ID查询，只返回指定列
     *
     * @param entityType   实体类
     * @param id           ID
     * @param selectFields select列
     * @return 当个当前实体类
     */
    default <E> E getById(Class<E> entityType, Serializable id, Getter<E>... selectFields) {
        return this.getWithQueryFun(entityType, (baseQuery -> {
            TableInfo tableInfo = Tables.get(entityType);
            WhereUtil.appendIdWhere(baseQuery.$where(), tableInfo, id);
            if (Objects.nonNull(selectFields) && selectFields.length > 0) {
                baseQuery.select(selectFields);
            }
        }));
    }

    /**
     * 单个查询
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 当个当前实体
     */
    default <E> E get(Class<E> entityType, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);

        BaseQuery<?, E> query = MapperCmdBuilderUtil.buildQuery(entityType, where);
        return this.get(query, false);
    }


    /**
     * 单个查询
     *
     * @param entityType 实体类Q
     * @param consumer   query consumer
     * @return 当个当前实体
     */
    default <E> E getWithQueryFun(Class<E> entityType, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return this.get(MapperCmdBuilderUtil.buildQuery(entityType, consumer), false);
    }


    /**
     * 是否存在
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 是否存在
     */
    default <E> boolean exists(Class<E> entityType, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.exists(MapperCmdBuilderUtil.buildQuery(entityType, where, baseQuery -> {
            baseQuery.select1();
        }));
    }


    /**
     * 是否存在
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 是否存在
     */
    default <E> boolean existsWithQueryFun(Class<E> entityType, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return this.exists(MapperCmdBuilderUtil.buildQuery(entityType, consumer));
    }

    /**
     * 实体类修改
     *
     * @param entity
     * @return 影响条数
     */
    default <E> int update(E entity) {
        return this.$update(new EntityUpdateContext(entity));
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 影响条数
     */
    default <E> int update(List<E> list) {
        int cnt = 0;
        for (E entity : list) {
            cnt += this.update(entity);
        }
        return cnt;
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list
     * @return 修改条数
     */
    default <E> int update(List<E> list, Getter<E>... forceUpdateFields) {
        Set<String> forceUpdateFieldsSet = new HashSet<>();
        if (Objects.nonNull(forceUpdateFields)) {
            for (Getter<?> column : forceUpdateFields) {
                forceUpdateFieldsSet.add(LambdaUtil.getName(column));
            }
        }

        int cnt = 0;
        for (E entity : list) {
            cnt += this.$update(new EntityUpdateContext(entity, forceUpdateFieldsSet));
        }
        return cnt;
    }

    /**
     * 实体类修改
     *
     * @param entity
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 返回修改条数
     */
    default <E> int update(E entity, Getter<E>... forceUpdateFields) {
        Set<String> forceUpdateFieldsSet = new HashSet<>();
        if (Objects.nonNull(forceUpdateFields)) {
            for (Getter getter : forceUpdateFields) {
                forceUpdateFieldsSet.add(LambdaUtil.getName(getter));
            }
        }
        return this.$update(new EntityUpdateContext<>(entity, forceUpdateFieldsSet));
    }


    default <E> int update(E entity, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.update(entity, where, (Getter[]) null);
    }

    default <E> int update(E entity, Where where, Getter<E>... forceUpdateFields) {
        Set<String> forceUpdateFieldsSet = new HashSet<>();
        if (Objects.nonNull(forceUpdateFields)) {
            for (Getter getter : forceUpdateFields) {
                forceUpdateFieldsSet.add(LambdaUtil.getName(getter));
            }
        }
        return this.$update(new EntityUpdateWithWhereContext(entity, where, forceUpdateFieldsSet));
    }

    /**
     * model修改 部分字段修改
     *
     * @param model 实体类model
     * @return 修改的条数
     */
    default <E> int update(Model<E> model) {
        return this.$update(new ModelUpdateContext<>(model));
    }

    /**
     * model修改 部分字段修改
     *
     * @param model             实体类model
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 修改的条数
     */
    default <E> int update(Model<E> model, Getter<E>... forceUpdateFields) {
        Set<String> forceUpdateFieldsSet = new HashSet<>();
        if (Objects.nonNull(forceUpdateFields)) {
            for (Getter getter : forceUpdateFields) {
                forceUpdateFieldsSet.add(LambdaUtil.getName(getter));
            }
        }
        return this.$update(new ModelUpdateContext<>(model, forceUpdateFieldsSet));
    }

    default <E> int update(Model<E> model, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.update(model, where, (Getter<E>) null);
    }

    default <E> int update(Model<E> model, Where where, Getter<E>... forceUpdateFields) {
        Set<String> forceUpdateFieldsSet = new HashSet<>();
        if (Objects.nonNull(forceUpdateFields)) {
            for (Getter getter : forceUpdateFields) {
                forceUpdateFieldsSet.add(LambdaUtil.getName(getter));
            }
        }
        return this.$update(new ModelUpdateWithWhereContext(model, where, forceUpdateFieldsSet));
    }


    /**
     * 根据id删除
     *
     * @param entityType 实体类
     * @param id         ID
     * @return 影响的数量
     */
    default <E> int deleteById(Class<E> entityType, Serializable id) {
        TableInfo tableInfo = Tables.get(entityType);
        return this.delete(entityType, where -> WhereUtil.appendIdWhere(where, tableInfo, id));
    }


    /**
     * 批量删除多个
     *
     * @param entityType 实体类
     * @param ids        多个ID
     * @return 影响的数量
     */
    default <E> int deleteByIds(Class<E> entityType, Serializable... ids) {
        if (ids == null || ids.length < 1) {
            throw new RuntimeException("ids array can't be empty");
        }
        TableInfo tableInfo = Tables.get(entityType);
        return this.delete(entityType, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }

    /**
     * 批量删除多个
     *
     * @param entityType 实体类
     * @param ids        多个ID
     * @return 影响的数量
     */
    default <E> int deleteByIds(Class<E> entityType, List<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ids list can't be empty");
        }
        return this.deleteByIds(entityType, ids.toArray(new Serializable[ids.size()]));
    }

    /**
     * 根据实体类删除
     *
     * @param entity 实体类实例
     * @return 影响的数量
     */
    default <E> int delete(E entity) {
        if (Objects.isNull(entity)) {
            return 0;
        }
        Class entityType = entity.getClass();
        TableInfo tableInfo = Tables.get(entityType);

        return this.delete(entityType, where -> {
            WhereUtil.appendIdWhere(where, tableInfo, TableInfoUtil.getEntityIdValue(tableInfo, entity));
            WhereUtil.appendVersionWhere(where, tableInfo, entity);
        });
    }

    /**
     * 多个删除
     *
     * @param list 实体类实例list
     * @return 修改条数
     */
    default <E> int delete(List<E> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        int length = list.size();
        Class<?> entityType = list.get(0).getClass();
        List<Serializable> ids = new LinkedList<>();
        TableInfo tableInfo = Tables.get(entityType);
        for (int i = 0; i < length; i++) {
            ids.add(TableInfoUtil.getEntityIdValue(tableInfo, list.get(i)));
        }
        return this.deleteByIds(entityType, ids);
    }


    /**
     * 根据id删除
     *
     * @param entityType 实体类
     * @param entityType 对应的实体类
     * @return 影响的数量
     */
    default <E> int delete(Class<E> entityType, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.delete(entityType, where);
    }

    /**
     * 根据where删除
     *
     * @param entityType 实体类
     * @param where      where
     * @return 影响的数量
     */
    default <E> int delete(Class<E> entityType, Where where) {
        if (!where.hasContent()) {
            throw new RuntimeException("delete has no where condition content ");
        }
        TableInfo tableInfo = Tables.get(entityType);
        if (LogicDeleteUtil.isNeedLogicDelete(tableInfo)) {
            //逻辑删除处理
            return LogicDeleteUtil.logicDelete(this, tableInfo, where);
        }
        Delete delete = new Delete(where);
        delete.delete(entityType);
        delete.from(entityType);
        return this.delete(delete);
    }

    /**
     * 列表查询
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 当个当前实体
     */
    default <E> List<E> list(Class<E> entityType, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.list(entityType, where, (Getter<E>[]) null);
    }

    default <E> List<E> list(Class<E> entityType, Where where, Getter<E>... selectFields) {
        BaseQuery<?, E> query = MapperCmdBuilderUtil.buildQuery(entityType, where, baseQuery -> {
            if (Objects.nonNull(selectFields) && selectFields.length > 0) {
                baseQuery.select(selectFields);
            }
        });
        return this.list(query, false);
    }

    /**
     * 列表查询
     *
     * @param entityType 实体类
     * @param consumer   query consumer
     * @return 当个当前实体
     */
    default <E> List<E> listWithQueryFun(Class<E> entityType, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return this.list(MapperCmdBuilderUtil.buildQuery(entityType, consumer), false);
    }


    /**
     * 游标查询
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 当个当前实体
     */
    default <E> Cursor<E> cursor(Class<E> entityType, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.cursor(entityType, where, (Getter<E>[]) null);
    }

    default <E> Cursor<E> cursor(Class<E> entityType, Where where, Getter<E>... selectFields) {
        return this.cursor(MapperCmdBuilderUtil.buildQuery(entityType, where, baseQuery -> {
            if (Objects.nonNull(selectFields) && selectFields.length > 0) {
                baseQuery.select(selectFields);
            }
        }), false);
    }

    /**
     * 游标查询
     *
     * @param entityType 实体类
     * @param consumer   query consumer
     * @return 当个当前实体
     */
    default <E> Cursor<E> cursorWithQueryFun(Class<E> entityType, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return this.cursor(MapperCmdBuilderUtil.buildQuery(entityType, consumer), false);
    }

    /**
     * count查询
     *
     * @param entityType 实体类
     * @param consumer   where consumer
     * @return 返回count数
     */
    default <E> Integer count(Class<E> entityType, Consumer<Where> consumer) {
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.count(MapperCmdBuilderUtil.buildQuery(entityType, where, baseQuery -> {
            baseQuery.selectCount1();
        }), false);
    }

    /**
     * count查询
     *
     * @param entityType 实体类
     * @param consumer   query consumer
     * @return 返回count数
     */
    default <E> Integer countWithQueryFun(Class<E> entityType, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return this.count(MapperCmdBuilderUtil.buildQuery(entityType, consumer), false);
    }

    /**
     * 分页查询
     *
     * @param consumer where consumer
     * @param pager    分页参数
     * @return 分页结果
     */
    default <E, P extends Pager<E>> P paging(Class<E> entityType, P pager, Consumer<Where> consumer) {
        return this.paging(entityType, consumer, pager, (Getter<E>[]) null);
    }

    default <E, P extends Pager<E>> P paging(Class<E> entityType, Consumer<Where> consumer, P pager, Getter<E>... selectFields) {
        pager.setOptimize(false);
        Where where = WhereUtil.create();
        consumer.accept(where);

        return this.paging(MapperCmdBuilderUtil.buildQuery(entityType, where, baseQuery -> {
            if (Objects.nonNull(selectFields) && selectFields.length > 0) {
                baseQuery.select(selectFields);
            }
        }), pager);
    }

    /**
     * count查询
     *
     * @param entityType 实体类
     * @param consumer   query consumer
     * @return 返回count数
     */
    default <E, P extends Pager<E>> P pagingWithQueryFun(Class<E> entityType, P pager, Consumer<BaseQuery<? extends BaseQuery, E>> consumer) {
        return this.paging(MapperCmdBuilderUtil.buildQuery(entityType, consumer), pager);
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <E, K> Map<K, E> mapWithKey(GetterFun<E, K> mapKey, Serializable... ids) {
        if (Objects.isNull(ids) || ids.length < 1) {
            return Collections.emptyMap();
        }
        return this.mapWithKey(mapKey, Arrays.asList(ids));
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <E, K> Map<K, E> mapWithKey(GetterFun<E, K> mapKey, List<Serializable> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(mapKey);
        Where where = WhereUtil.create();
        TableInfo tableInfo = Tables.get(lambdaFieldInfo.getType());
        WhereUtil.appendIdsWhere(where, tableInfo, ids);
        return this.mapWithKey(mapKey, MapperCmdBuilderUtil.buildQuery(lambdaFieldInfo.getType(), where), false);
    }


    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey   map的key
     * @param consumer where consumer
     * @param <K>      map的key的类型
     * @return 一个map
     */
    default <K, E> Map<K, E> mapWithKey(GetterFun<E, K> mapKey, Consumer<Where> consumer) {
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(mapKey);
        Where where = WhereUtil.create();
        consumer.accept(where);
        return this.mapWithKey(mapKey, MapperCmdBuilderUtil.buildQuery(lambdaFieldInfo.getType(), where), false);
    }
}
