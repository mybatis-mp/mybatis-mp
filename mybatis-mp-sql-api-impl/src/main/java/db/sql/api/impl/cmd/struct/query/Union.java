package db.sql.api.impl.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.cmd.struct.query.IUnion;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class Union implements IUnion {

    private final char[] operator;

    private final IQuery unionQuery;

    public Union(IQuery unionQuery) {
        this(SqlConst.UNION, unionQuery);
    }

    public Union(char[] operator, IQuery unionQuery) {
        this.operator = operator;
        this.unionQuery = unionQuery;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(this.operator);
        unionQuery.sql(module, this, context, sqlBuilder);
        return sqlBuilder;
    }


    public char[] getOperator() {
        return operator;
    }

    @Override
    public IQuery getUnionQuery() {
        return unionQuery;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.unionQuery);
    }
}
