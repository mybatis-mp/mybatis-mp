package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.sql.executor.Update;

import java.util.Collections;
import java.util.Set;

public class EntityUpdateContext<T> extends SQLCmdUpdateContext {

    public EntityUpdateContext(T t) {
        this(t, Collections.emptySet());
    }

    public EntityUpdateContext(T t, Set<String> forceUpdateFields) {
        super(createCmd(t, forceUpdateFields));
    }

    private static Update createCmd(Object t, Set<String> forceUpdateFields) {
        return EntityUpdateCmdCreateUtil.create(t, forceUpdateFields);
    }
}
