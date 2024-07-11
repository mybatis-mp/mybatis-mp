package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.executor.AbstractInsert;

public abstract class BaseInsert<T extends BaseInsert<T>> extends AbstractInsert<T, MybatisCmdFactory> {

    public BaseInsert() {
        super(new MybatisCmdFactory());
    }

    @Override
    @SafeVarargs
    public final <T2> T field(Getter<T2>... fields) {
        return super.field(fields);
    }

    /**************以下为去除警告************/

    @Override
    @SafeVarargs
    public final T field(TableField... fields) {
        return super.field(fields);
    }

    /**************以上为去除警告************/
}
