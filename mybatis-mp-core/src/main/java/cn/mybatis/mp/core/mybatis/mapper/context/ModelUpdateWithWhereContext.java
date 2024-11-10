package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.db.Model;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Set;

public class ModelUpdateWithWhereContext<T extends Model> extends SQLCmdUpdateContext {

    public ModelUpdateWithWhereContext(T t, Where where, boolean allFieldForce, Set<String> forceUpdateFields) {
        super(createCmd(t, where, allFieldForce, forceUpdateFields));
    }

    private static Update createCmd(Model t, Where where, boolean allFieldForce, Set<String> forceUpdateFields) {
        return ModelUpdateCmdCreateUtil.create(t, where, forceUpdateFields, allFieldForce);
    }
}
