package cn.mybatis.mp.core.mvc.impl;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mvc.Dao;
import cn.mybatis.mp.core.mybatis.mapper.MapperCmdBuilderUtil;
import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.chain.DeleteChain;
import cn.mybatis.mp.core.sql.executor.chain.InsertChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import cn.mybatis.mp.core.sql.executor.chain.UpdateChain;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.*;

public abstract class DaoImpl<T, K> implements Dao<T, K> {

    protected MybatisMapper<T> mapper;
    protected volatile Class<K> idType;
    private volatile TableInfo tableInfo;

    public DaoImpl() {

    }

    public DaoImpl(MybatisMapper<T> mapper) {
        this.setMapper(mapper);
    }

    protected MybatisMapper<T> getMapper() {
        return this.mapper;
    }

    protected void setMapper(MybatisMapper<T> mapper) {
        this.mapper = mapper;
    }

    protected TableInfo getTableInfo() {
        if (Objects.isNull(tableInfo)) {
            tableInfo = Tables.get(getMapper().getEntityType());
        }
        return tableInfo;
    }

    @Override
    public Class<K> getIdType() {
        if (idType == null) {
            List<?> genericTypes = GenericUtil.getGenericSuperClass(this.getClass());
            idType = (Class<K>) genericTypes.get(genericTypes.size() - 1);
        }
        return idType;
    }

    protected QueryChain<T> queryChain() {
        return QueryChain.of(getMapper());
    }

    protected UpdateChain updateChain() {
        return UpdateChain.of(getMapper());
    }

    protected InsertChain insertChain() {
        return InsertChain.of(getMapper());
    }

    protected DeleteChain deleteChain() {
        return DeleteChain.of(getMapper());
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
        return getMapper().getById((Serializable) id);
    }

    @Override
    public T getById(K id, Getter<T>... selectFields) {
        this.checkIdType();
        return getMapper().getById((Serializable) id, selectFields);
    }

    @Override
    public int save(T entity) {
        return getMapper().save(entity);
    }

    @Override
    public int save(Collection<T> list) {
        return getMapper().save(list);
    }

    @Override
    public int save(Model<T> model) {
        return getMapper().save(model);
    }

    @Override
    public int update(T entity) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(entity);
    }

    @Override
    public int saveOrUpdate(T entity) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().saveOrUpdate(entity);
    }

    @Override
    public int update(Collection<T> list) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(list);
    }

    @Override
    public int update(T entity, Getter<T>... forceUpdateFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(entity, forceUpdateFields);
    }

    @Override
    public int update(Model<T> model) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(model);
    }

    @Override
    public int update(Model<T> model, Getter<T>... forceUpdateFields) {
        if (!getTableInfo().isHasMultiId()) {
            this.checkIdType();
        }
        return getMapper().update(model, forceUpdateFields);
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
        return getMapper().deleteById((Serializable) id);
    }

    @Override
    public int deleteByIds(K... ids) {
        this.checkIdType();
        return getMapper().deleteByIds(ids);
    }

    @Override
    public int deleteByIds(Collection<K> ids) {
        this.checkIdType();
        return getMapper().deleteByIds((Collection<Serializable>) ids);
    }

    @Override
    public Map<K, T> map(K... ids) {
        return this.map(Arrays.asList(ids));
    }

    @Override
    public Map<K, T> map(Collection<K> ids) {
        this.checkIdType();
        Where where = WhereUtil.create();
        BaseQuery<?, T> query = MapperCmdBuilderUtil.buildQuery(getMapper().getEntityType(), where);
        query.optimizeOptions(optimizeOptions -> optimizeOptions.disableAll());
        return getMapper().mapWithKey(getTableInfo().getSingleIdFieldInfo(true).getField().getName(), query);
    }
}
