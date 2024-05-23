package db.sql.api.impl.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.cmd.struct.insert.IInsertSelect;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class InsertSelect implements IInsertSelect<IQuery> {

    private final IQuery query;

    public InsertSelect(IQuery query) {
        this.query = query;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.query);
    }

    @Override
    public IQuery getSelectQuery() {
        return query;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.BLANK);
        query.sql(module, parent, context, sqlBuilder);
        return sqlBuilder;
    }
}
