package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class Instr extends BasicFunction<Instr> {

    private final Cmd str;

    public Instr(Cmd key, String str) {
        super(SqlConst.INSTR, key);
        this.str = Methods.cmd(str);
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (context.getDbType() == DbType.SQL_SERVER) {
            sqlBuilder.append(" CHARINDEX").append(SqlConst.BRACKET_LEFT);
            this.str.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.DELIMITER);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }
        if (context.getDbType() == DbType.PGSQL) {
            sqlBuilder.append("strpos");
        } else {
            sqlBuilder.append(operator);
        }
        sqlBuilder.append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        this.str.sql(module, this, context, sqlBuilder);

        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key, this.str);
    }
}