package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.db.Model;

import java.util.Set;

public class ModelUpdateContext<T extends Model> extends SQLCmdUpdateContext {

    public ModelUpdateContext(T t, Set<String> forceUpdateFields, boolean allFieldForce) {
        super(createCmd(t, forceUpdateFields, allFieldForce));
    }

    private static Update createCmd(Model t, Set<String> forceUpdateFields, boolean allFieldForce) {
        return ModelUpdateCmdCreateUtil.create(t, forceUpdateFields, allFieldForce);
    }
}
