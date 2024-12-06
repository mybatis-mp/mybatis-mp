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
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.*;
import cn.mybatis.mp.core.sql.executor.chain.DeleteChain;
import cn.mybatis.mp.core.sql.executor.chain.InsertChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import cn.mybatis.mp.core.sql.executor.chain.UpdateChain;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    protected UpdateChain updateChain() {
        UpdateChain updateChain = UpdateChain.of(getMapper(), getEntityType());
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


    @Override
    public T getById(ID id) {
        return this.getById(id, null);
    }

    @Override
    public T getById(ID id, Getter<T>... selectFields) {
        this.checkIdType();
        return GetMethodUtil.getById(getBasicMapper(), getTableInfo(), (Serializable) id, selectFields);
    }

    @Override
    public int save(T entity) {
        return this.save(entity, false);
    }

    @Override
    public int save(T entity, Getter<T>... forceFields) {
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), entity, false, forceFields);
    }

    @Override
    public int save(T entity, boolean allFieldForce) {
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), entity, allFieldForce, null);
    }

    @Override
    public int saveOrUpdate(T entity) {
        return this.saveOrUpdate(entity, false);
    }

    @Override
    public int saveOrUpdate(T entity, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), entity, allFieldForce);
    }

    @Override
    public int saveOrUpdate(T entity, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), entity, false, forceFields);
    }

    @Override
    public int saveOrUpdate(Collection<T> list) {
        return this.saveOrUpdate(list, false);
    }

    @Override
    public int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), list, allFieldForce, (Getter<T>[]) null);
    }

    @Override
    public int saveOrUpdate(Collection<T> list, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), list, false, forceFields);
    }

    @Override
    public int save(Collection<T> list) {
        return this.save(list, false);
    }

    @Override
    public int save(Collection<T> list, boolean allFieldForce) {
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), list, allFieldForce, (Getter<T>[]) null);
    }

    @Override
    public int save(Collection<T> list, Getter<T>... forceFields) {
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), list, false, forceFields);
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
    public <M extends Model<T>> int saveOrUpdate(M model) {
        return this.saveOrUpdate(model, false);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), Models.get(model.getClass()), model, allFieldForce, null);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, Getter<M>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), Models.get(model.getClass()), model, false, forceFields);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list) {
        return this.saveOrUpdateModel(list, false);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), list, allFieldForce, null);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Getter<M>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), list, false, forceFields);
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

}
