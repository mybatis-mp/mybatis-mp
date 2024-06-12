package db.sql.api.impl.cmd.struct.delete;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.struct.delete.IDeleteTable;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractDelete;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class DeleteTable implements IDeleteTable<IDataset> {

    private final IDataset[] tables;

    public DeleteTable(IDataset[] tables) {
        this.tables = tables;
    }

    @Override
    public IDataset[] getTables() {
        return this.tables;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.DELETE);
        if (this.tables == null || this.tables.length < 1) {
            return sqlBuilder;
        }
        AbstractDelete delete = (AbstractDelete) module;
        if (this.tables.length == 1 && delete.getJoins() == null) {
            this.tables[0].as(null);
            return sqlBuilder;
        }
        int length = this.tables.length;
        for (int i = 0; i < length; i++) {
            IDataset dataset = this.tables[i];
            if (i != 0) {
                sqlBuilder.append(SqlConst.DELIMITER);
            }
            if (dataset.getAlias() != null) {
                sqlBuilder.append(dataset.getAlias());
            } else {
                if (dataset instanceof Table) {
                    Table table = (Table) dataset;
                    sqlBuilder.append(table.getName());
                }
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, (Object[]) this.tables);
    }
}
