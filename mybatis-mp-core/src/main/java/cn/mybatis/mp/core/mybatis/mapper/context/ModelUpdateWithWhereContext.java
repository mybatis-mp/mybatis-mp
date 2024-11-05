package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.db.Model;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Set;

public class ModelUpdateWithWhereContext<T extends Model> extends SQLCmdUpdateContext {

    public ModelUpdateWithWhereContext(T t, Where where, Set<String> forceUpdateFields, boolean allFieldForce) {
        super(createCmd(t, where, forceUpdateFields, allFieldForce));
    }

    private static Update createCmd(Model t, Where where, Set<String> forceUpdateFields, boolean allFieldForce) {
        return ModelUpdateCmdCreateUtil.create(t, where, forceUpdateFields, allFieldForce);
    }
}
