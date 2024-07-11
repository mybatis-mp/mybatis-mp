package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.io.Serializable;

import static db.sql.api.impl.tookit.SqlConst.IF;

public class If extends BasicFunction<If> {

    private final Cmd value;

    private final Cmd thenValue;

    public If(ICondition condition, Serializable value, Serializable thenValue) {
        this(condition, Methods.cmd(value), Methods.cmd(thenValue));
    }

    public If(ICondition condition, Cmd value, Serializable thenValue) {
        this(condition, value, Methods.cmd(thenValue));
    }

    public If(ICondition condition, Serializable value, Cmd thenValue) {
        this(condition, Methods.cmd(value), thenValue);
    }

    public If(ICondition condition, Cmd value, Cmd thenValue) {
        super(IF, condition);
        this.value = value;
        this.thenValue = thenValue;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        this.value.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        this.thenValue.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key, this.value, this.thenValue);
    }
}
