package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Right extends BasicFunction<Right> {

    private final int length;

    public Right(Cmd key, int length) {
        super(SqlConst.RIGHT, key);
        this.length = length;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (context.getDbType() == DbType.ORACLE) {
            sqlBuilder.append(" SUBSTR");
        } else {
            sqlBuilder.append(operator);
        }

        sqlBuilder.append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);

        if (context.getDbType() == DbType.ORACLE) {
            sqlBuilder.append(SqlConst.DELIMITER).append("LENGTH(");
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(") - " + (length - 1));
        }

        sqlBuilder.append(SqlConst.DELIMITER).append(this.length);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

}