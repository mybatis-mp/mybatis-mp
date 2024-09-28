package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Truncate extends BasicFunction<Truncate> {

    private final int precision;

    public Truncate(Cmd key, int precision) {
        super(null, key);
        this.precision = precision;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.TRUNCATE(context.getDbType())).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER).append(this.precision);
        if (context.getDbType() == DbType.SQL_SERVER) {
            sqlBuilder.append(",1");
        }
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

}