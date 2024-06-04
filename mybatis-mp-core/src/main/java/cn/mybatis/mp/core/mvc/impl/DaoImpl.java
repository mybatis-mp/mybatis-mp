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
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.core.util.WhereUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class DaoImpl<T, K> implements Dao<T, K> {

    protected MybatisMapper<T> mapper;
    protected Class<K> idType;
    private TableInfo tableInfo;

    public DaoImpl() {

    }

    public DaoImpl(MybatisMapper<T> mapper) {
        this.setMapper(mapper);
    }

    protected void setMapper(MybatisMapper<T> mapper) {
        this.mapper = mapper;
    }

    protected TableInfo getTableInfo() {
        if (Objects.isNull(tableInfo)) {
            tableInfo = Tables.get(mapper.getEntityType());
        }
        return tableInfo;
    }

    @Override
    public Class<K> getIdType() {
        if (idType == null) {
            idType = (Class<K>) GenericUtil.getGenericSuperClass(this.getClass()).get(1);
        }
        return idType;
    }

    protected QueryChain<T> queryChain() {
        return QueryChain.of(mapper);
    }

    protected UpdateChain updateChain() {
        return UpdateChain.of(mapper);
    }

    protected InsertChain insertChain() {
        return InsertChain.of(mapper);
    }

    protected DeleteChain deleteChain() {
        return DeleteChain.of(mapper);
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
        return mapper.getById((Serializable) id);
    }

    @Override
    public T getById(K id, Getter<T>... selectFields) {
        this.checkIdType();
        return mapper.getById((Serializable) id, selectFields);
    }

    @Override
    public int save(T entity) {
        return mapper.save(entity);
    }

    @Override
    public int save(List<T> list) {
        return mapper.save(list);
    }

    @Override
    public int save(Model<T> model) {
        return mapper.save(model);
    }

    @Override
    public int update(T entity) {
        this.checkIdType();
        return mapper.update(entity);
    }

    @Override
    public int update(List<T> list) {
        return mapper.update(list);
    }

    @Override
    public int update(T entity, Getter<T>... forceUpdateFields) {
        this.checkIdType();
        return mapper.update(entity, forceUpdateFields);
    }

    @Override
    public int update(Model<T> model) {
        if (getIdType() == Void.class) {
            throw new RuntimeException("Not Supported");
        }
        return mapper.update(model);
    }

    @Override
    public int update(Model<T> model, Getter<T>... forceUpdateFields) {
        return mapper.update(model, forceUpdateFields);
    }

    @Override
    public int delete(T entity) {
        this.checkIdType();
        return mapper.delete(entity);
    }

    @Override
    public int delete(List<T> list) {
        return mapper.delete(list);
    }

    @Override
    public int deleteById(K id) {
        this.checkIdType();
        return mapper.deleteById((Serializable) id);
    }

    @Override
    public int deleteByIds(K... ids) {
        this.checkIdType();
        return mapper.deleteByIds(ids);
    }

    @Override
    public int deleteByIds(List<K> ids) {
        this.checkIdType();
        return mapper.deleteByIds((List<Serializable>) ids);
    }

    @Override
    public Map<K, T> map(K... ids) {
        return this.map(Arrays.asList(ids));
    }

    @Override
    public Map<K, T> map(List<K> ids) {
        this.checkIdType();
        Where where = WhereUtil.create();
        BaseQuery<?, T> query = MapperCmdBuilderUtil.buildQuery(mapper.getEntityType(), where);
        return mapper.mapWithKey(getTableInfo().getIdFieldInfo().getField().getName(), query, false);
    }
}
