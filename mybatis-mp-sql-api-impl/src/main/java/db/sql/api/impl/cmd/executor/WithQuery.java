package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IWithQuery;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.condition.Exists;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.struct.From;
import db.sql.api.impl.cmd.struct.query.With;
import db.sql.api.impl.tookit.SqlConst;

/**
 * 子查询
 */
public class WithQuery extends AbstractWithQuery<WithQuery, CmdFactory> implements Dataset<WithQuery, DatasetField> {

    private final String alias;

    public WithQuery(String alias) {
        super(new CmdFactory("wt"));
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public WithQuery as(String alias) {
        throw new RuntimeException("not support");
    }
}
