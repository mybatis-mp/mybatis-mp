package db.sql.api.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.tookit.CmdUtils;

import java.util.List;

public class CmdList implements Cmd {

    private final char[] operator;

    private final List<Cmd> cmdList;

    public CmdList(char[] operator, List<Cmd> cmdList) {
        this.operator = operator;
        this.cmdList = cmdList;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(this.operator);
        CmdUtils.join(module, this, context, sqlBuilder, this.cmdList);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.cmdList);
    }
}
