package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.sql.executor.Update;

import java.util.Set;

public class EntityUpdateContext<T> extends SQLCmdUpdateContext {

    public EntityUpdateContext(TableInfo tableInfo, T t, boolean allFieldForce, Set<String> forceFields) {
        super(createCmd(tableInfo, t, allFieldForce, forceFields));
    }

    private static Update createCmd(TableInfo tableInfo, Object t, boolean allFieldForce, Set<String> forceFields) {
        return EntityUpdateCmdCreateUtil.create(tableInfo, t, allFieldForce, forceFields);
    }
}
