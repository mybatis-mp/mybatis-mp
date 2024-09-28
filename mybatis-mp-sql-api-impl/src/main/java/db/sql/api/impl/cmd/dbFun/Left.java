package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Left extends BasicFunction<Left> {

    private final int length;

    public Left(Cmd key, int length) {
        super(SqlConst.LEFT, key);
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
            sqlBuilder.append(SqlConst.DELIMITER).append(1);
        }
        sqlBuilder.append(SqlConst.DELIMITER).append(this.length);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

}