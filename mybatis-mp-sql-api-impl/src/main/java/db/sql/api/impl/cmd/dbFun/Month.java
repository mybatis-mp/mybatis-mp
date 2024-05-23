package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Month extends BasicFunction<Month> {
    public Month(Cmd key) {
        super(null, key);
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.MONTH(context.getDbType()));
        if (context.getDbType() != DbType.PGSQL && context.getDbType() != DbType.ORACLE) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
        }

        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}
