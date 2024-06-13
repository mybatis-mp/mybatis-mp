package db.sql.api.impl.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.Objects;

public class WithRecursive implements Cmd {

    private String[] params;

    public WithRecursive() {

    }

    public WithRecursive(String[] params) {
        this.params = params;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (Objects.nonNull(params) && params.length > 0) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            CmdUtils.join(sqlBuilder, this.params, SqlConst.DELIMITER);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return false;
    }
}
