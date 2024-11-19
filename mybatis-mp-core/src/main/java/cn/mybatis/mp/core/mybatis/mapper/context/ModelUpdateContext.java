package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.db.Model;

import java.util.Set;

public class ModelUpdateContext<T extends Model> extends SQLCmdUpdateContext {

    public ModelUpdateContext(T t, boolean allFieldForce, Set<String> forceFields) {
        super(createCmd(t, allFieldForce, forceFields));
    }

    private static Update createCmd(Model t, boolean allFieldForce, Set<String> forceFields) {
        return ModelUpdateCmdCreateUtil.create(t, forceFields, allFieldForce);
    }
}
