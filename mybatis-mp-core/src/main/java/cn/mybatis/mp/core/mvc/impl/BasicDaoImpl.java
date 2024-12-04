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

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mvc.Dao;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
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
import java.util.Objects;

/**
 * 专门给不想写Mapper的人用
 *
 * @param <T>
 * @param <K>
 */
public class BasicDaoImpl<T, K> implements Dao<T, K> {

    protected BasicMapper mapper;
    protected volatile Class<T> entityType;
    protected volatile Class<K> idType;
    private volatile TableInfo tableInfo;

    public BasicDaoImpl() {

    }

    public BasicDaoImpl(BasicMapper mapper) {
        this.setMapper(mapper);
    }

    protected BasicMapper getMapper() {
        return this.mapper;
    }

    protected void setMapper(BasicMapper mapper) {
        this.mapper = mapper;
    }

    protected TableInfo getTableInfo() {
        if (Objects.isNull(tableInfo)) {
            tableInfo = Tables.get(getEntityType());
        }
        return tableInfo;
    }

    private void loadGenericType() {
        List<?> genericTypes = GenericUtil.getGenericSuperClass(this.getClass());
        entityType = (Class<T>) genericTypes.get(genericTypes.size() - 2);
        idType = (Class<K>) genericTypes.get(genericTypes.size() - 1);
    }

    protected Class<T> getEntityType() {
        if (entityType == null) {
            this.loadGenericType();
        }
        return entityType;
    }

    @Override
    public Class<K> getIdType() {
        if (idType == null) {
            this.loadGenericType();
        }
        return idType;
    }

    protected QueryChain<T> queryChain() {
        return QueryChain.of(getMapper(), getEntityType());
    }

    protected UpdateChain updateChain() {
        return UpdateChain.of(getMapper(), getEntityType());
    }

    protected InsertChain insertChain() {
        return InsertChain.of(getMapper(), getEntityType());
    }

    protected DeleteChain deleteChain() {
        return DeleteChain.of(getMapper(), getEntityType());
    }

    private void checkIdType() {
        Class<K> clazz = getIdType();
        if (clazz == null || clazz == Void.class) {
            throw new RuntimeException("Not Supported");
        }
    }

    @Override
    public T getById(K id) {
        this.checkIdType();
        return getMapper().getById(getEntityType(), (Serializable) id);
    }

    @Override
    public T getById(K id, Getter<T>... selectFields) {
        this.checkIdType();
        return getMapper().getById(getEntityType(), (Serializable) id, selectFields);
    }

    @Override
    public int save(T entity) {
        return getMapper().save(entity);
    }

    @Override
    public int save(T entity, Getter<T>... forceFields) {
        return getMapper().save(entity, forceFields);
    }

    @Override
    public int save(T entity, boolean allFieldForce) {
        return getMapper().save(entity, allFieldForce);
    }

    @Override
    public int saveOrUpdate(T entity) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(entity);
    }

    @Override
    public int saveOrUpdate(T entity, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(entity, allFieldForce);
    }

    @Override
    public int saveOrUpdate(T entity, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(entity, forceFields);
    }

    @Override
    public int saveOrUpdate(Collection<T> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(list);
    }

    @Override
    public int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(list, allFieldForce);
    }

    @Override
    public int saveOrUpdate(Collection<T> list, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(list, forceFields);
    }

    @Override
    public int save(Collection<T> list) {
        return getMapper().save(list);
    }

    @Override
    public int save(Collection<T> list, boolean allFieldForce) {
        return getMapper().save(list, allFieldForce);
    }

    @Override
    public int save(Collection<T> list, Getter<T>... forceFields) {
        return getMapper().save(list, forceFields);
    }

    @Override
    public <M extends Model<T>> int save(M model) {
        return getMapper().save(model);
    }

    @Override
    public <M extends Model<T>> int save(M model, Getter<M>... forceFields) {
        return getMapper().save(model, forceFields);
    }

    @Override
    public <M extends Model<T>> int save(M model, boolean allFieldForce) {
        return getMapper().save(model, allFieldForce);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list) {
        return getMapper().saveModel(list);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list, boolean allFieldForce) {
        return getMapper().saveModel(list, allFieldForce);
    }

    @Override
    public <M extends Model<T>> int saveModel(Collection<M> list, Getter<M>... forceFields) {
        return getMapper().saveModel(list, forceFields);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(model);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(model, allFieldForce);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdate(M model, Getter<M>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(model, forceFields);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdateModel(list);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, boolean allFieldForce) {
        return getMapper().saveOrUpdateModel(list, allFieldForce);
    }

    @Override
    public <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Getter<M>... forceFields) {
        return getMapper().saveOrUpdateModel(list, forceFields);
    }

    @Override
    public int update(T entity) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(entity);
    }

    @Override
    public int update(T entity, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(entity, allFieldForce);
    }

    @Override
    public int update(Collection<T> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(list);
    }

    @Override
    public int update(Collection<T> list, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(list, allFieldForce);
    }

    @Override
    public int update(Collection<T> list, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(list, forceFields);
    }

    @Override
    public int update(T entity, Getter<T>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(entity, forceFields);
    }

    @Override
    public <M extends Model<T>> int update(M model) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(model);
    }

    @Override
    public <M extends Model<T>> int update(M model, Getter<M>... forceFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(model, forceFields);
    }

    @Override
    public <M extends Model<T>> int update(M model, boolean allFieldForce) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(model, allFieldForce);
    }

    @Override
    public <M extends Model<T>> int updateModel(Collection<M> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().updateModel(list);
    }

    @Override
    public <M extends Model<T>> int updateModel(Collection<M> list, Getter<M>... forceFields) {
        return getMapper().updateModel(list, forceFields);
    }

    @Override
    public <M extends Model<T>> int updateModel(Collection<M> list, boolean allFieldForce) {
        return getMapper().updateModel(list, allFieldForce);
    }

    @Override
    public int delete(T entity) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().delete(entity);
    }

    @Override
    public int delete(Collection<T> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().delete(list);
    }

    @Override
    public int deleteById(K id) {
        this.checkIdType();
        return getMapper().deleteById(getEntityType(), (Serializable) id);
    }

    @Override
    public int deleteByIds(K... ids) {
        this.checkIdType();
        return getMapper().deleteByIds(getEntityType(), ids);
    }

    @Override
    public int deleteByIds(Collection<K> ids) {
        this.checkIdType();
        return getMapper().deleteByIds(getEntityType(), (Collection<Serializable>) ids);
    }

    @Override
    public Map<K, T> map(K... ids) {
        this.checkIdType();
        return (Map<K, T>) getMapper().map(getEntityType(), ids);
    }

    @Override
    public Map<K, T> map(Collection<K> ids) {
        this.checkIdType();
        return (Map<K, T>) getMapper().map(getEntityType(), (Collection<Serializable>) ids);
    }
}
