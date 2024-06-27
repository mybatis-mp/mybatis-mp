package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Count extends BasicFunction<Count> {

    private boolean distinct = false;

    public Count(Cmd key) {
        super(SqlConst.COUNT, key);
    }

    public Count(Cmd key, boolean distinct) {
        super(SqlConst.COUNT, key);
        this.distinct = distinct;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        if (distinct) {
            sqlBuilder.append(SqlConst.DISTINCT);
        }
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}
