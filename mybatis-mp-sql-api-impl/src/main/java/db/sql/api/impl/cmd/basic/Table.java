package db.sql.api.impl.cmd.basic;


import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.struct.From;
import db.sql.api.impl.cmd.struct.Join;
import db.sql.api.impl.tookit.SqlConst;

import java.util.Objects;

public class Table implements Dataset<Table, TableField> {

    protected String alias;
    protected String prefix;
    private String name;
    private String forceIndex;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String alias) {
        this(name);
        this.alias = alias;
    }

    @Override
    public TableField $(String name) {
        return new TableField(this, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(DbType dbType) {
        return dbType.wrap(this.name);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public Table setAlias(String alias) {
        return as(alias);
    }

    public Table as(String alias) {
        this.alias = alias;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Table setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(getName(context.getDbType()));
        if (getAlias() != null) {
            sqlBuilder.append(SqlConst.BLANK).append(getAlias());
        }
        if (parent instanceof From || parent instanceof Join) {
            if (Objects.nonNull(forceIndex) && !SqlConst.S_EMPTY.equals(forceIndex)) {
                sqlBuilder.append(SqlConst.FORCE_INDEX(context.getDbType(), forceIndex));
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return false;
    }

    public Table forceIndex(String forceIndex) {
        this.forceIndex = forceIndex;
        return this;
    }
}
