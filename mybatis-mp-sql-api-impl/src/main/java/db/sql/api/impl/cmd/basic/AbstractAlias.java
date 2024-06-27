package db.sql.api.impl.cmd.basic;

import db.sql.api.Getter;
import db.sql.api.cmd.basic.Alias;
import db.sql.api.impl.tookit.SqlUtil;

public abstract class AbstractAlias<T extends AbstractAlias<T>> implements Alias<T> {

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

    public <T2> T as(Getter<T2> aliasGetter) {
        return this.as(SqlUtil.getAsName(aliasGetter));
    }
}
