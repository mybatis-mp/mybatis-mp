package db.sql.api.impl.cmd.basic;


import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.tookit.SqlConst;

public class AllField extends AbstractDatasetField<AllField> {

    public final static AllField ALL = new AllField();

    public AllField() {
        super(null, SqlConst.ALL);
    }

    public AllField(IDataset table) {
        super(table, SqlConst.ALL);
    }

    @Override
    public AllField as(String alias) {
        throw new RuntimeException("AllField不能设置别名");
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (module instanceof Select) {
            if (getTable() != null) {
                if (getTable().getAlias() != null) {
                    sqlBuilder.append(getTable().getAlias()).append(SqlConst.DOT);
                }
            }
        }
        return sqlBuilder.append(SqlConst.ALL);
    }
}
