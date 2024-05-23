package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.db.Model;

import java.util.Collections;
import java.util.Set;

public class ModelUpdateContext<T extends Model> extends SQLCmdUpdateContext {

    public ModelUpdateContext(T t) {
        this(t, Collections.emptySet());
    }

    public ModelUpdateContext(T t, Set<String> forceUpdateFields) {
        super(createCmd(t, forceUpdateFields));
    }

    private static Update createCmd(Model t, Set<String> forceUpdateFields) {
        return ModelUpdateCmdCreateUtil.create(t, forceUpdateFields);
    }
}
