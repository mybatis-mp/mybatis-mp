package cn.mybatis.mp.core.sql.executor;

import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.TableField;

public class MpTable extends db.sql.api.impl.cmd.basic.Table {

    public MpTable(String name) {
        super(name);
    }

    public MpTable(String name, String alias) {
        super(name, alias);
    }

    @Override
    public <E> TableField $(Getter<E> column) {
        return super.$(column);
    }
}
