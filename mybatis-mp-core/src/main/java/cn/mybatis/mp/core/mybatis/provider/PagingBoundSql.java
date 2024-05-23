package cn.mybatis.mp.core.mybatis.provider;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;


public class PagingBoundSql extends BoundSql {

    private final String sql;

    private final BoundSql delegate;

    public PagingBoundSql(Configuration configuration, String sql, BoundSql delegate) {
        super(configuration, null, null, null);
        this.sql = sql;
        this.delegate = delegate;
    }


    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public List<ParameterMapping> getParameterMappings() {
        return delegate.getParameterMappings();
    }

    @Override
    public Object getParameterObject() {
        return delegate.getParameterObject();
    }

    @Override
    public boolean hasAdditionalParameter(String name) {
        return delegate.hasAdditionalParameter(name);
    }

    @Override
    public void setAdditionalParameter(String name, Object value) {
        delegate.setAdditionalParameter(name, value);
    }

    @Override
    public Object getAdditionalParameter(String name) {
        return delegate.getAdditionalParameter(name);
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        return delegate.getAdditionalParameters();
    }
}
