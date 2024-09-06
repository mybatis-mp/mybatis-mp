package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.mybatis.mapper.context.PreparedContext;
import cn.mybatis.mp.core.mybatis.provider.PreparedSQLProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface DbRunner {

    default int execute(String sql, Object... params) {
        return this.$execute(new PreparedContext(sql, params));
    }

    @UpdateProvider(value = PreparedSQLProvider.class, method = PreparedSQLProvider.SQL)
    int $execute(PreparedContext preparedContext);
}
