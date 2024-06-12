package db.sql.api.impl.cmd.basic;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IField;
import db.sql.api.impl.cmd.dbFun.FunctionInterface;
import db.sql.api.impl.tookit.SqlUtil;

public abstract class AbstractField<T extends AbstractField<T>> implements Value, IField<T>, FunctionInterface {

    protected String alias;

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public T as(String alias) {
        this.alias = alias;
        return (T) this;
    }

    @Override
    public <T2> T as(Getter<T2> aliasGetter) {
        return this.as(SqlUtil.getAsName(aliasGetter));
    }
}
