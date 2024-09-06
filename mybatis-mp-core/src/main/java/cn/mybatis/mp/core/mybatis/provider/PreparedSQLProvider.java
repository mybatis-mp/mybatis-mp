package cn.mybatis.mp.core.mybatis.provider;

import cn.mybatis.mp.core.mybatis.mapper.context.PreparedContext;

public class PreparedSQLProvider {

    public static final String SQL = "sql";

    public static String sql(PreparedContext preparedContext) {
        return preparedContext.getSql();
    }
}
