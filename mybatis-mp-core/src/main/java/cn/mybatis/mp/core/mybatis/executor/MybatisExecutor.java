package cn.mybatis.mp.core.mybatis.executor;

import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdQueryContext;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public class MybatisExecutor implements Executor {

    private final Executor delegate;

    public MybatisExecutor(Executor delegate) {
        this.delegate = delegate;
        delegate.setExecutorWrapper(this);
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        return this.delegate.update(ms, parameter);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        return this.delegate.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        return this.delegate.query(ms, parameter, rowBounds, resultHandler);
    }

    @Override
    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        return this.delegate.queryCursor(ms, parameter, rowBounds);
    }

    @Override
    public List<BatchResult> flushStatements() throws SQLException {
        return this.delegate.flushStatements();
    }

    @Override
    public void commit(boolean required) throws SQLException {
        this.delegate.commit(required);
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        this.delegate.rollback(required);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        CacheKey cacheKey = this.delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
        if (parameterObject instanceof SQLCmdQueryContext) {
            SQLCmdQueryContext context = (SQLCmdQueryContext) parameterObject;
            cacheKey.updateAll(context.getParameters());
            cacheKey.update(context.getExecution().getReturnType().getName());
        }
        return cacheKey;
    }

    @Override
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return this.delegate.isCached(ms, key);
    }

    @Override
    public void clearLocalCache() {
        this.delegate.clearLocalCache();
    }

    @Override
    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
        this.delegate.deferLoad(ms, resultObject, property, key, targetType);
    }

    @Override
    public Transaction getTransaction() {
        return this.delegate.getTransaction();
    }

    @Override
    public void close(boolean forceRollback) {
        this.delegate.close(forceRollback);
    }

    @Override
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override
    public void setExecutorWrapper(Executor executor) {
        this.delegate.setExecutorWrapper(executor);
    }
}
