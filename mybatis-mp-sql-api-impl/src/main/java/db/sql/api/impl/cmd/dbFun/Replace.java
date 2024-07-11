package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class Replace extends BasicFunction<Replace> {

    private final Cmd target;

    private final Cmd replacement;

    public Replace(Cmd value, String target, String replacement) {
        super(SqlConst.REPLACE, value);
        this.target = Methods.cmd(target);
        this.replacement = Methods.cmd(replacement);
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        this.target.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        this.replacement.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key, this.target, this.replacement);
    }
}
