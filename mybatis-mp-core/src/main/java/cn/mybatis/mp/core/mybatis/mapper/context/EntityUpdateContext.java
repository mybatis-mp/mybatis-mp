package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.sql.executor.Update;

import java.util.Set;

public class EntityUpdateContext<T> extends SQLCmdUpdateContext {

    public EntityUpdateContext(T t, Set<String> forceUpdateFields, boolean allFieldForce) {
        super(createCmd(t, forceUpdateFields, allFieldForce));
    }

    private static Update createCmd(Object t, Set<String> forceUpdateFields, boolean allFieldForce) {
        return EntityUpdateCmdCreateUtil.create(t, forceUpdateFields, allFieldForce);
    }
}
