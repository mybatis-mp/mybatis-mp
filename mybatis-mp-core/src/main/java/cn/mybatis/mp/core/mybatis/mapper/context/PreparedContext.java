package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.mybatis.configuration.PreparedParameterContext;

public class PreparedContext implements PreparedParameterContext {

    private final String sql;

    private final Object[] params;

    public PreparedContext(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public Object[] getParameters() {
        return this.params;
    }
}
