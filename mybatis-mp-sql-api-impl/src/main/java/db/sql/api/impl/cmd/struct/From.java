package db.sql.api.impl.cmd.struct;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.struct.IFrom;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.ArrayList;
import java.util.List;

public class From implements IFrom {

    private final List<IDataset> tables = new ArrayList<>(2);

    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> From append(DATASET table) {
        this.tables.add(table);
        return this;
    }

    @Override
    public List<IDataset> getTables() {
        return tables;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.FROM);
        return CmdUtils.join(module, this, context, sqlBuilder, this.tables, SqlConst.DELIMITER);
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.tables);
    }
}
