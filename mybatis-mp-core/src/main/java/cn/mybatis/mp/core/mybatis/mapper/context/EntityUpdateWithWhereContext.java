package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.sql.executor.Update;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Set;

public class EntityUpdateWithWhereContext<T> extends SQLCmdUpdateContext {

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where) {
        this(tableInfo, t, where, null);
    }

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where, Set<String> forceFields) {
        this(tableInfo, t, where, false, forceFields);
    }

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where, boolean allFieldForce) {
        this(tableInfo, t, where, allFieldForce, null);
    }

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where, boolean allFieldForce, Set<String> forceFields) {
        super(createCmd(tableInfo, t, where, allFieldForce, forceFields));
    }


    private static Update createCmd(TableInfo tableInfo, Object t, Where where, boolean allFieldForce, Set<String> forceFields) {
        return EntityUpdateCmdCreateUtil.create(tableInfo, t, where, allFieldForce, forceFields);
    }
}
