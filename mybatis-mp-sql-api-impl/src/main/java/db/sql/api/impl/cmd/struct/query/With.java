package db.sql.api.impl.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IWithQuery;
import db.sql.api.cmd.struct.query.IWith;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class With implements IWith<With> {

    private final IWithQuery withQuery;

    public With(IWithQuery withQuery) {
        this.withQuery = withQuery;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        Cmd recursive = this.withQuery.getRecursive();

        if (Objects.nonNull(recursive) && (context.getDbType() == DbType.H2 || context.getDbType() ==
                DbType.MYSQL || context.getDbType() == DbType.MARIA_DB || context.getDbType() == DbType.PGSQL)) {
            sqlBuilder.append(SqlConst.RECURSIVE);
        }

        sqlBuilder.append(this.withQuery.getAlias());
        if (Objects.nonNull(recursive)) {
            sqlBuilder = recursive.sql(module, this, context, sqlBuilder);
        }

        sqlBuilder.append(SqlConst.AS).append(SqlConst.BRACKET_LEFT);
        this.withQuery.sql(module, this, context, sqlBuilder).append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.withQuery);
    }
}
