package db.sql.api.impl.cmd.condition;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class Empty<COLUMN, V> extends BaseCondition<Cmd, Void> {

    private final Cmd field;

    public Empty(Cmd field) {
        super(SqlConst.BLANK);
        this.field = field;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (context.getDbType() == DbType.ORACLE) {
            return new IsNull(this.field).sql(module, parent, context, sqlBuilder);
        }
        return new Eq(this.field, "").sql(module, parent, context, sqlBuilder);
    }

    @Override
    public Cmd getField() {
        return field;
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.field);
    }
}
