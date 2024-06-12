package db.sql.api.impl.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.tookit.SqlConst;

import java.util.Objects;

public class Column extends AbstractDatasetField<Column> {

    public Column(String name) {
        super(null, name);
    }

    public Column(IDataset dataset, String name) {
        super(dataset, name);
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.BLANK).append(getName(context.getDbType()));
        if (Objects.nonNull(this.alias)) {
            if (parent instanceof Select) {
                sqlBuilder.append(SqlConst.BLANK).append(this.alias);
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return false;
    }
}
