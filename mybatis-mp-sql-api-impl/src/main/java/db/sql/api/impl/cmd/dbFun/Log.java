package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Log extends BasicFunction<Log> {

    private final Integer number;

    public Log(Cmd key) {
        this(key, null);
    }

    public Log(Cmd key, Integer number) {
        super(SqlConst.LOG, key);
        this.number = number;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        if (this.key != null) {
            this.key.sql(module, this, context, sqlBuilder);
        }

        if (number != null) {
            if (this.key != null) {
                sqlBuilder.append(SqlConst.DELIMITER);
            }
            sqlBuilder.append(this.number);
        }

        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}
