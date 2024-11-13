package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.sql.executor.BaseQuery;

public class MapKeySQLCmdQueryContext extends SQLCmdQueryContext {

    private String key;

    public MapKeySQLCmdQueryContext(String key, BaseQuery execution) {
        super(execution);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
