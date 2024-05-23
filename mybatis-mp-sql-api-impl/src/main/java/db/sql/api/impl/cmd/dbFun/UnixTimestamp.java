package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class UnixTimestamp extends BasicFunction<UnixTimestamp> {


    public UnixTimestamp(Cmd key) {
        super(null, key);
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {

        if (context.getDbType() == DbType.ORACLE) {
            sqlBuilder.append(" TO_NUMBER(CAST(");
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(" AS DATE)- TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60 ");
            return sqlBuilder;
        }

        sqlBuilder.append(SqlConst.UNIX_TIMESTAMP(context.getDbType()));
        sqlBuilder.append(SqlConst.BRACKET_LEFT);

        this.key.sql(module, this, context, sqlBuilder);

        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}
