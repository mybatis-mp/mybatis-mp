package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import db.sql.api.impl.cmd.ConditionFactory;

public final class Where extends db.sql.api.impl.cmd.struct.Where<Where> {

    public Where() {
        super(new ConditionFactory(new MybatisCmdFactory()));
    }

    public static Where create() {
        return new Where();
    }
}
