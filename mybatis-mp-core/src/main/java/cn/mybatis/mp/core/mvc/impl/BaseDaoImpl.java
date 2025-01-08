/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.mvc.impl;

import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mvc.Dao;
import cn.mybatis.mp.core.mybatis.mapper.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveBatchStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveOrUpdateStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveStrategy;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.*;
import cn.mybatis.mp.core.sql.executor.Insert;
import cn.mybatis.mp.core.sql.executor.chain.DeleteChain;
import cn.mybatis.mp.core.sql.executor.chain.InsertChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import cn.mybatis.mp.core.sql.executor.chain.UpdateChain;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.page.IPager;
import db.sql.api.Getter;
import db.sql.api.GetterFun;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseDaoImpl<M extends BaseMapper, T, ID> implements Dao<T, ID> {

    protected M mapper;

    private volatile Class<T> entityType;
    private volatile Class<ID> idType;
    private volatile TableInfo tableInfo;

    protected M getMapper() {
        return mapper;
    }

    protected void setMapper(M mapper) {
        this.mapper = mapper;
    }

    abstract BasicMapper getBasicMapper();

    protected TableInfo getTableInfo() {
        if (tableInfo == null) {
            tableInfo = Tables.get(getEntityType());
        }
        return tableInfo;
    }

    private void loadGenericType() {
        List<?> genericTypes = GenericUtil.getGenericSuperClass(this.getClass());
        entityType = (Class<T>) genericTypes.get(genericTypes.size() - 2);
        idType = (Class<ID>) genericTypes.get(genericTypes.size() - 1);
    }

    @Override
    public Class<T> getEntityType() {
        if (entityType == null) {
            this.loadGenericType();
        }
        return entityType;
    }

    @Override
    public Class<ID> getIdType() {
        if (idType == null) {
            this.loadGenericType();
        }
        return idType;
    }

    protected void checkIdType() {
        Class<ID> clazz = getIdType();
        if (clazz == null || clazz == Void.class) {
            throw new RuntimeException("Not Supported");
        }
    }

    protected QueryChain<T> queryChain() {
        QueryChain<T> queryChain = QueryChain.of(getMapper(), getEntityType());
        queryChain.$().cacheTableInfo(getTableInfo());
        return queryChain;
    }

    protected QueryChain<T> queryChain(Where where) {
        QueryChain<T> queryChain = QueryChain.of(getMapper(), getEntityType(), where);
        queryChain.$().cacheTableInfo(getTableInfo());
        return queryChain;
    }

    protected UpdateChain updateChain() {
        UpdateChain updateChain = UpdateChain.of(getMapper(), getEntityType());
        updateChain.$().cacheTableInfo(getTableInfo());
        return updateChain;
    }

    protected UpdateChain updateChain(Where where) {
        UpdateChain updateChain = UpdateChain.of(getMapper(), getEntityType(), where);
        updateChain.$().cacheTableInfo(getTableInfo());
        return updateChain;
    }

    protected InsertChain insertChain() {
        InsertChain insertChain = InsertChain.of(getMapper(), getEntityType());
        insertChain.$().cacheTableInfo(getTableInfo());
        return insertChain;
    }

    protected DeleteChain deleteChain() {
        DeleteChain deleteChain = DeleteChain.of(getMapper(), getEntityType());
        deleteChain.$().cacheTableInfo(getTableInfo());
        return deleteChain;
    }

    protected DeleteChain deleteChain(Where where) {
        DeleteChain deleteChain = DeleteChain.of(getMapper(), getEntityType(), where);
        deleteChain.$().cacheTableInfo(getTableInfo());
        return deleteChain;
    }


    @Override
    public T getById(ID id) {
        return this.getById(id, (Getter<T>[]) null);
    }

    @Override
    public T getById(ID id, Getter<T>... selectFields) {
        this.checkIdType();
        return GetMethodUtil.getById(getBasicMapper(), getTableInfo(), (Serializable) id, selectFields);
    }

    /**
     * 单个查询
     *
     * @param consumer where consumer
     * @return 单个当前实体
     */
    protected T get(Consumer<Where> consumer) {
        return this.get(consumer, (Getter<T>[]) null);
    }

    /**
     * 单个查询
     *
     * @param consumer     where consumer
     * @param selectFields select列
     * @return 单个当前实体
     */
    protected T get(Consumer<Where> consumer, Getter<T>... selectFields) {
        return GetMethodUtil.get(getBasicMapper(), getTableInfo(), consumer, selectFields);
    }

    /**
     * 单个查询
     *
     * @param where where
     * @return 单个当前实体
     */
    protected T get(Where where) {
        return get(where, (Getter<T>[]) null);
    }

    /**
     * 单个查询
     *
     * @param where        where
     * @param selectFields select列
     * @return 单个当前实体
     */
    protected T get(Where where, Getter<T>... selectFields) {
        return GetMethodUtil.get(getBasicMapper(), getTableInfo(), where, selectFields);
    }

    /**
     * 是否存在
     *
     * @param consumer where consumer
     * @return 是否存在
     */
    protected boolean exists(Consumer<Where> consumer) {
        return ExistsMethodUtil.exists(getBasicMapper(), getTableInfo(), consumer);
    }

    /**
     * 是否存在
     *
     * @param where
     * @return 是否存在
     */
    protected boolean exists(Where where) {
        return ExistsMethodUtil.exists(getBasicMapper(), getTableInfo(), where);
    }

    /**
     * 总数
     *
     * @return count数
     */
    protected int countAll() {
        return CountMethodUtil.countAll(getBasicMapper(), getTableInfo());
    }

    /**
     * 是否存在
     *
     * @param consumer where consumer
     * @return count数
     */
    protected int count(Consumer<Where> consumer) {
        return CountMethodUtil.count(getBasicMapper(), getTableInfo(), consumer);
    }

    /**
     * 是否存在
     *
     * @param where
     * @return count数
     */
    protected int count(Where where) {
        return CountMethodUtil.count(getBasicMapper(), getTableInfo(), where);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids  指定ID
     * @param <ID> ID
     * @return 返回结果列表
     */
    protected <ID extends Serializable> List<T> listByIds(ID... ids) {
        this.checkIdType();
        return this.listByIds(ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>         ID
     * @return 返回结果列表
     */
    protected <ID extends Serializable> List<T> listByIds(ID[] ids, Getter<T>... selectFields) {
        this.checkIdType();
        return ListMethodUtil.listByIds(getBasicMapper(), getTableInfo(), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids  指定ID
     * @param <ID> ID
     * @return 返回结果列表
     */
    protected <ID extends Serializable> List<T> listByIds(Collection<ID> ids) {
        this.checkIdType();
        return this.listByIds(ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>         ID
     * @return 返回结果列表
     */
    protected <ID extends Serializable> List<T> listByIds(Collection<ID> ids, Getter<T>... selectFields) {
        this.checkIdType();
        return ListMethodUtil.listByIds(getBasicMapper(), getTableInfo(), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer where consumer
     * @return 返回结果列表
     */
    protected List<T> list(Consumer<Where> consumer) {
        return this.list(consumer, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer     where consumer
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    protected List<T> list(Consumer<Where> consumer, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), null, consumer, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit    条数
     * @param consumer where consumer
     * @return 返回结果列表
     */
    protected List<T> list(Integer limit, Consumer<Where> consumer) {
        return this.list(limit, consumer, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit        条数
     * @param consumer     where consumer
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    protected List<T> list(Integer limit, Consumer<Where> consumer, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), limit, consumer, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param where where
     * @return 返回结果列表
     */
    protected List<T> list(Where where) {
        return this.list(where, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param where        where
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    protected List<T> list(Where where, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), null, where, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit 条数
     * @param where where
     * @return 返回结果列表
     */
    protected List<T> list(Integer limit, Where where) {
        return this.list(limit, where, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param limit        条数
     * @param where        where
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    protected List<T> list(Integer limit, Where where, Getter<T>... selectFields) {
        return ListMethodUtil.list(getBasicMapper(), getTableInfo(), limit, where, selectFields);
    }

    /**
     * 查所有
     *
     * @return
     */
    protected List<T> listAll() {
        return ListMethodUtil.listAll(getBasicMapper(), getTableInfo());
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids  指定ID
     * @param <ID>
     * @return 返回结果列表
     */
    protected <ID extends Serializable> Cursor<T> cursorByIds(ID... ids) {
        this.checkIdType();
        return this.cursorByIds(ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>
     * @return 返回结果列表
     */
    protected <ID extends Serializable> Cursor<T> cursorByIds(ID[] ids, Getter<T>... selectFields) {
        this.checkIdType();
        return CursorMethodUtil.cursorByIds(getBasicMapper(), getTableInfo(), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids  指定ID
     * @param <ID>
     * @return 返回结果列表
     */
    protected <ID extends Serializable> Cursor<T> cursorByIds(Collection<ID> ids) {
        this.checkIdType();
        return this.cursorByIds(ids, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param ids          指定ID
     * @param selectFields select指定列
     * @param <ID>
     * @return 返回结果列表
     */
    protected <ID extends Serializable> Cursor<T> cursorByIds(Collection<ID> ids, Getter<T>... selectFields) {
        this.checkIdType();
        return CursorMethodUtil.cursorByIds(getBasicMapper(), getTableInfo(), ids, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer where consumer
     * @return 返回结果列表
     */
    protected Cursor<T> cursor(Consumer<Where> consumer) {
        return this.cursor(consumer, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param consumer     where consumer
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    protected Cursor<T> cursor(Consumer<Where> consumer, Getter<T>... selectFields) {
        return CursorMethodUtil.cursor(getBasicMapper(), getTableInfo(), consumer, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param where where
     * @return 返回结果列表
     */
    protected Cursor<T> cursor(Where where) {
        return this.cursor(where, (Getter<T>[]) null);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @param where        where
     * @param selectFields select指定列
     * @return 返回结果列表
     */
    protected Cursor<T> cursor(Where where, Getter<T>... selectFields) {
        return CursorMethodUtil.cursor(getBasicMapper(), getTableInfo(), where, selectFields);
    }

    /**
     * 列表查询,返回类型，当前实体类
     *
     * @return
     */
    protected Cursor<T> cursorAll() {
        return CursorMethodUtil.cursorAll(getBasicMapper(), getTableInfo());
    }

    /**
     * 分页查询
     *
     * @param consumer where consumer
     * @param pager    分页参数
     * @return 分页结果
     */
    protected <P extends IPager<T>> P paging(P pager, Consumer<Where> consumer) {
        return this.paging(pager, consumer, (Getter<T>[]) null);
    }

    /**
     * 分页查询
     *
     * @param consumer     where consumer
     * @param pager        pager
     * @param selectFields select指定列
     * @return
     */
    protected <P extends IPager<T>> P paging(P pager, Consumer<Where> consumer, Getter<T>... selectFields) {
        return PagingMethodUtil.paging(getBasicMapper(), getTableInfo(), pager, consumer, selectFields);
    }

    /**
     * 分页查询
     *
     * @param where where
     * @param pager 分页参数
     * @return 分页结果
     */
    protected <P extends IPager<T>> P paging(P pager, Where where) {
        return this.paging(pager, where, (Getter<T>[]) null);
    }

    protected <P extends IPager<T>> P paging(P pager, Where where, Getter<T>... selectFields) {
        return PagingMethodUtil.paging(getBasicMapper(), getTableInfo(), pager, where, selectFields);
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    protected <K, ID extends Serializable> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, ID... ids) {
        this.checkIdType();
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), getTableInfo(), mapKey, ids);
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    protected <K, ID extends Serializable> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Collection<ID> ids) {
        this.checkIdType();
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), getTableInfo(), mapKey, ids);
    }


    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey   map的key
     * @param consumer where consumer
     * @param <K>      map的key的类型
     * @return 一个map
     */
    protected <K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Consumer<Where> consumer) {
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), getTableInfo(), mapKey, consumer);
    }

    @Override
    public Map<ID, T> map(ID... ids) {
        this.checkIdType();
        return (Map<ID, T>) MapWithKeyMapperUtil.map(getBasicMapper(), getTableInfo(), (Serializable[]) ids);
    }

    @Override
    public Map<ID, T> map(Collection<ID> ids) {
        this.checkIdType();
        return (Map<ID, T>) MapWithKeyMapperUtil.map(getBasicMapper(), getTableInfo(), (Collection<Serializable>) ids);
    }

    @Override
    public int save(T entity, Consumer<SaveStrategy<T>> saveStrategy) {
        SaveStrategy<T> strategy = new SaveStrategy<>();
        saveStrategy.accept(strategy);
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), entity, strategy);
    }

    @Override
    public int save(T entity) {
        return this.save(entity, false);
    }

    @Override
    public int save(T entity, boolean allFieldForce) {
        return this.save(entity, saveStrategy -> {
            saveStrategy.allFieldSave(allFieldForce);
        });
    }

    @Override
    public int save(T entity, Getter<T>... forceFields) {
        return this.save(entity, saveStrategy -> {
            saveStrategy.forceFields(forceFields);
        });
    }

    @Override
    public int saveOrUpdate(T entity, Consumer<SaveOrUpdateStrategy<T>> saveOrUpdateStrategy) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        SaveOrUpdateStrategy strategy = new SaveOrUpdateStrategy();
        saveOrUpdateStrategy.accept(strategy);
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), entity, strategy);
    }

    @Override
    public int saveOrUpdate(T entity) {
        return this.saveOrUpdate(entity, false);
    }

    @Override
    public int saveOrUpdate(T entity, boolean allFieldForce) {
        return this.saveOrUpdate(entity, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.allField(allFieldForce);
        });
    }

    @Override
    public int saveOrUpdate(T entity, Getter<T>... forceFields) {
        return this.saveOrUpdate(entity, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.forceFields(forceFields);
        });
    }

    @Override
    public int saveOrUpdate(Collection<T> list, Consumer<SaveOrUpdateStrategy<T>> saveOrUpdateStrategy) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        SaveOrUpdateStrategy strategy = new SaveOrUpdateStrategy();
        saveOrUpdateStrategy.accept(strategy);
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), list, strategy);
    }

    @Override
    public int saveOrUpdate(Collection<T> list) {
        return this.saveOrUpdate(list, false);
    }

    @Override
    public int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        return this.saveOrUpdate(list, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.allField(allFieldForce);
        });
    }

    @Override
    public int saveOrUpdate(Collection<T> list, Getter<T>... forceFields) {
        return this.saveOrUpdate(list, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.forceFields(forceFields);
        });
    }

    @Override
    public int save(Collection<T> list, Consumer<SaveStrategy<T>> saveStrategy) {
        SaveStrategy strategy = new SaveStrategy();
        saveStrategy.accept(strategy);
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), list, strategy);
    }

    @Override
    public int save(Collection<T> list) {
        return this.save(list, false);
    }

    @Override
    public int save(Collection<T> list, boolean allFieldForce) {
        return this.save(list, (saveStrategy) -> {
            saveStrategy.allFieldSave(allFieldForce);
        });
    }

    @Override
    public int save(Collection<T> list, Getter<T>... forceFields) {
        return this.save(list, (saveStrategy) -> {
            saveStrategy.forceFields(forceFields);
        });
    }

    public int saveBatch(Collection<T> list, Consumer<SaveBatchStrategy<T>> consumer) {
        SaveBatchStrategy strategy = new SaveBatchStrategy();
        consumer.accept(strategy);
        return SaveMethodUtil.saveBatch(getBasicMapper(), new Insert(), getTableInfo(), list, strategy);
    }

    public int saveBatch(Collection<T> list) {
        return SaveMethodUtil.saveBatch(getBasicMapper(), new Insert(), getTableInfo(), list);
    }

    @Override
    public <M extends Model<T>> int save(M model) {
        return this.save(model, false);
    }

    @Override
    public <M extends Model<T>> int save(M model, Getter<M>... forceFields) {
        return SaveModelMethodUtil.save(getBasicMapper(), model, false, forceFields);
    }

    @Override
    public <M extends Model<T>> int save(M model, boolean allFieldForce) {
        return SaveModelMethodUtil.save(getBasicMapper(), model, allFieldForce, null);
    }

    @Override
    public <M extends Model<T>> int save(M model, Consumer<SaveStrategy<M>> saveStrategy) {
        SaveStrategy<M> strategy = new SaveStrategy<>();
        saveStrategy.accept(strategy);
        return SaveModelMethodUtil.save(getBasicMapper(), model, strategy);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list) {
        return this.saveModel(list, false);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list, boolean allFieldForce) {
        return SaveModelMethodUtil.save(getBasicMapper(), list, allFieldForce, null);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list, Getter<M>... forceFields) {
        return SaveModelMethodUtil.save(getBasicMapper(), list, false, forceFields);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list, Consumer<SaveStrategy<M>> saveStrategy) {
        SaveStrategy<M> strategy = new SaveStrategy<>();
        saveStrategy.accept(strategy);
        return SaveModelMethodUtil.save(getBasicMapper(), list, strategy);
    }

    public <M extends Model<T>> int saveModelBatch(Collection<M> list) {
        return SaveModelMethodUtil.saveBatch(getBasicMapper(), new Insert(), list);
    }

    public <M extends Model<T>> int saveModelBatch(Collection<M> list, Consumer<SaveBatchStrategy<M>> consumer) {
        SaveBatchStrategy strategy = new SaveBatchStrategy();
        consumer.accept(strategy);
        return SaveModelMethodUtil.saveBatch(getBasicMapper(), new Insert(), list, strategy);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, Consumer<SaveOrUpdateStrategy<M>> saveOrUpdateStrategy) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        SaveOrUpdateStrategy strategy = new SaveOrUpdateStrategy();
        saveOrUpdateStrategy.accept(strategy);
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), Models.get(model.getClass()), model, strategy);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model) {
        return this.saveOrUpdate(model, false);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, boolean allFieldForce) {
        return this.saveOrUpdate(model, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.allField(allFieldForce);
        });
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, Getter<M>... forceFields) {
        return this.saveOrUpdate(model, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.forceFields(forceFields);
        });
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Consumer<SaveOrUpdateStrategy<M>> saveOrUpdateStrategy) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        SaveOrUpdateStrategy strategy = new SaveOrUpdateStrategy();
        saveOrUpdateStrategy.accept(strategy);
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), list, strategy);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list) {
        return this.saveOrUpdateModel(list, false);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, boolean allFieldForce) {
        return this.saveOrUpdateModel(list, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.allField(allFieldForce);
        });
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Getter<M>... forceFields) {
        return this.saveOrUpdateModel(list, (saveOrUpdateStrategy) -> {
            saveOrUpdateStrategy.forceFields(forceFields);
        });
    }

    @Override
    public int update(T entity) {
        return this.update(entity, false);
    }

    @Override
    public int update(T entity, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(entity.getClass()), entity, allFieldForce, (Getter<T>[]) null);
    }

    @Override
    public int update(T entity, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(entity.getClass()), entity, false, forceFields);
    }

    /**
     * 动态条件修改
     *
     * @param entity   实体类
     * @param consumer where
     * @return 影响条数
     */
    protected int update(T entity, Consumer<Where> consumer) {
        return this.update(entity, false, consumer);
    }

    /**
     * 动态条件修改
     *
     * @param entity        实体类对象
     * @param allFieldForce 是否所有字段都修改，如果是null值，则变成NULL
     * @param consumer      where
     * @return 影响条数
     */
    protected int update(T entity, boolean allFieldForce, Consumer<Where> consumer) {
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), entity, allFieldForce, null, consumer);
    }

    /**
     * 动态where 修改
     *
     * @param entity      实体类对象
     * @param consumer    where
     * @param forceFields 强制更新指定，解决需要修改为null的需求
     * @return 影响条数
     */
    protected int update(T entity, Consumer<Where> consumer, Getter<T>... forceFields) {
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), entity, false, forceFields, consumer);
    }

    /**
     * 指定where 修改
     *
     * @param entity 实体类对象
     * @param where  where
     * @return 影响条数
     */
    protected int update(T entity, Where where) {
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), entity, false, null, where);
    }

    /**
     * 指定where 修改
     *
     * @param entity        实体类对象
     * @param where         where
     * @param allFieldForce 是否所有字段都修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    protected int update(T entity, boolean allFieldForce, Where where) {
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), entity, allFieldForce, null, where);
    }

    /**
     * 指定where 修改
     *
     * @param entity      实体类对象
     * @param where       where
     * @param forceFields 强制更新指定，解决需要修改为null的需求
     * @return 影响条数
     */
    protected int update(T entity, Where where, Getter<T>... forceFields) {
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), entity, false, forceFields, where);
    }

    @Override
    public int update(Collection<T> list) {
        return this.update(list, false);
    }

    @Override
    public int update(Collection<T> list, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), list, allFieldForce, (Getter<T>[]) null);
    }

    @Override
    public int update(Collection<T> list, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return UpdateMethodUtil.update(getBasicMapper(), getTableInfo(), list, false, forceFields);
    }

    @Override
    public <M extends Model<T>> int update(M model) {
        return this.update(model, false);
    }

    @Override
    public <M extends Model<T>> int update(M model, Getter<M>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return UpdateModelMethodUtil.update(getBasicMapper(), model, false, forceFields);
    }

    @Override
    public <M extends Model<T>> int update(M model, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return UpdateModelMethodUtil.update(getBasicMapper(), model, allFieldForce, (Getter<M>[]) null);
    }

    @Override
    public <M extends Model<T>> int updateModel(Collection<M> list) {
        return this.updateModel(list, false);
    }

    @Override
    public <M extends Model<T>> int updateModel(Collection<M> list, Getter<M>... forceFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), list, false, forceFields);
    }

    @Override
    public <M extends Model<T>> int updateModel(Collection<M> list, boolean allFieldForce) {
        return UpdateModelMethodUtil.update(getBasicMapper(), list, allFieldForce, null);
    }

    @Override
    public int delete(T entity) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), entity);
    }

    @Override
    public int delete(Collection<T> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), list);
    }

    @Override
    public int deleteById(ID id) {
        this.checkIdType();
        return DeleteMethodUtil.deleteById(getBasicMapper(), getTableInfo(), (Serializable) id);
    }

    @Override
    public int deleteByIds(ID... ids) {
        this.checkIdType();
        return DeleteMethodUtil.deleteByIds(getBasicMapper(), getTableInfo(), (Serializable[]) ids);
    }

    @Override
    public int deleteByIds(Collection<ID> ids) {
        this.checkIdType();
        return DeleteMethodUtil.deleteByIds(getBasicMapper(), getTableInfo(), (Collection<Serializable>) ids);
    }

    /**
     * 动态条件删除
     *
     * @param consumer
     * @return 影响条数
     */
    protected int delete(Consumer<Where> consumer) {
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), consumer);
    }

    /**
     * 动态条件删除
     *
     * @param where
     * @return 影响条数
     */
    protected int delete(Where where) {
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), where);
    }

    /**
     * 删除所有数据
     *
     * @return 影响条数
     */
    protected int deleteAll() {
        return DeleteMethodUtil.deleteAll(getBasicMapper(), getTableInfo());
    }

    /**
     * TRUNCATE TABLE
     *
     * @return 影响条数
     */
    protected int truncate() {
        return DeleteMethodUtil.truncate(getBasicMapper(), getTableInfo());
    }

}
