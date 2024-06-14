package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

import java.util.concurrent.TimeUnit;

public class DateAdd extends BasicFunction<DateAdd> {

    private final int n;

    private final TimeUnit timeUnit;

    public DateAdd(Cmd key, int n, TimeUnit timeUnit) {
        super(SqlConst.DATE_ADD, key);
        this.n = n;
        this.timeUnit = timeUnit;
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.DAYS.name().substring(0, TimeUnit.DAYS.name().length() - 1));
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (context.getDbType() == DbType.H2) {
            sqlBuilder.append("DATEADD").append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append(timeUnit.name(), 0, timeUnit.name().length() - 1);
            sqlBuilder.append(SqlConst.DELIMITER);
            sqlBuilder.append(n);
            sqlBuilder.append(SqlConst.DELIMITER);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        } else if (context.getDbType() == DbType.PGSQL) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append("+'");
            sqlBuilder.append(n);
            sqlBuilder.append(SqlConst.BLANK);
            sqlBuilder.append(timeUnit.name(), 0, timeUnit.name().length() - 1);
            sqlBuilder.append(SqlConst.SINGLE_QUOT);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        } else if (context.getDbType() == DbType.DM || context.getDbType() == DbType.SQL_SERVER) {
            sqlBuilder.append("DATEADD").append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append(timeUnit.name(), 0, timeUnit.name().length() - 1);
            sqlBuilder.append(SqlConst.DELIMITER);
            sqlBuilder.append(n);
            sqlBuilder.append(SqlConst.DELIMITER);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        } else if (context.getDbType() == DbType.ORACLE || context.getDbType() == DbType.KING_BASE) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append('+');
            sqlBuilder.append(SqlConst.INTERVAL).append(SqlConst.SINGLE_QUOT).append(this.n).append(SqlConst.SINGLE_QUOT);
            sqlBuilder.append(timeUnit.name(), 0, timeUnit.name().length() - 1);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        } else if (context.getDbType() == DbType.DB2) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append('+');
            sqlBuilder.append(this.n).append(SqlConst.BLANK);
            sqlBuilder.append(timeUnit.name());
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }

        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER).append(SqlConst.INTERVAL).append(this.n);
        sqlBuilder.append(SqlConst.BLANK);
        sqlBuilder.append(timeUnit.name(), 0, timeUnit.name().length() - 1);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}