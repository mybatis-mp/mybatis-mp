package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.sql.executor.Update;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Collections;
import java.util.Set;

public class EntityUpdateWithWhereContext<T> extends SQLCmdUpdateContext {

    public EntityUpdateWithWhereContext(T t, Where where) {
        this(t, where, Collections.emptySet());
    }

    public EntityUpdateWithWhereContext(T t, Where where, Set<String> forceUpdateFields) {
        super(createCmd(t, where, forceUpdateFields));
    }

    private static Update createCmd(Object t, Where where, Set<String> forceUpdateFields) {
        return EntityUpdateCmdCreateUtil.create(t, where, forceUpdateFields);
    }
}
