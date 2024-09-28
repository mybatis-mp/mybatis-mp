package db.sql.api.impl.cmd.dbFun.mysql;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.dbFun.BasicFunction;
import db.sql.api.impl.tookit.SqlConst;

public class FromUnixTime extends BasicFunction<FromUnixTime> {
    public FromUnixTime(Cmd key) {
        super(SqlConst.FROM_UNIXTIME, key);
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (context.getDbType() == DbType.ORACLE) {
            sqlBuilder.append("(TO_TIMESTAMP('1970-01-01', 'YYYY-MM-DD') + NUMTODSINTERVAL(");
            this.key.sql(module, parent, context, sqlBuilder);
            sqlBuilder.append(", 'SECOND'))");
            return sqlBuilder;
        } else if (context.getDbType() == DbType.PGSQL) {
            sqlBuilder.append("TO_TIMESTAMP(");
            this.key.sql(module, parent, context, sqlBuilder);
            sqlBuilder.append(")::TIMESTAMP");
            return sqlBuilder;
        } else if (context.getDbType() == DbType.SQL_SERVER) {
            sqlBuilder.append("DATEADD(s, CONVERT(BIGINT, ");
            this.key.sql(module, parent, context, sqlBuilder);
            sqlBuilder.append("), CONVERT(DATETIME, '1970-01-01'))");
            return sqlBuilder;
        }
        return super.sql(module, parent, context, sqlBuilder);
    }
}
