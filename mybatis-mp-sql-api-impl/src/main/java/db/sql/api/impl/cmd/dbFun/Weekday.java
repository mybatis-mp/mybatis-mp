package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

import java.time.LocalDate;

public class Weekday extends BasicFunction<Weekday> {
    public Weekday(Cmd key) {
        super(null, key);
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.parse("2023-10-11").getDayOfWeek().getValue());
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.WEEKDAY(context.getDbType()));
        if (context.getDbType() != DbType.SQL_SERVER) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
        }

        this.key.sql(module, this, context, sqlBuilder);
        if (context.getDbType() == DbType.ORACLE || context.getDbType() == DbType.PGSQL) {
            sqlBuilder.append(SqlConst.DELIMITER).append(" 'D'");
        }
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}
