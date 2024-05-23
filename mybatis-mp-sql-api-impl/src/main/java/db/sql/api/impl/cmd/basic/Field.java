package db.sql.api.impl.cmd.basic;

import db.sql.api.Getter;
import db.sql.api.cmd.basic.IColumn;
import db.sql.api.impl.cmd.dbFun.FunctionInterface;
import db.sql.api.impl.tookit.SqlUtil;

public abstract class Field<T extends Field<T>> implements IColumn, Value, FunctionInterface {

    protected String alias;

    public String getAlias() {
        return alias;
    }

    public T as(String alias) {
        this.alias = alias;
        return (T) this;
    }

    public <T2> T as(Getter<T2> aliasGetter) {
        return this.as(SqlUtil.getAsName(aliasGetter));
    }
}
