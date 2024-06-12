package db.sql.api.impl.cmd.struct;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.struct.IJoin;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.function.Function;

public class Join implements IJoin<Join, On, Table, TableField, Cmd, Object, ConditionChain> {

    private final IDataset mainTable;

    private final IDataset secondTable;

    private final JoinMode mode;

    private final On on;


    public Join(JoinMode mode, IDataset mainTable, IDataset secondTable, Function<Join, On> onFunction) {
        this.mode = mode;
        this.mainTable = mainTable;
        this.secondTable = secondTable;
        this.on = onFunction.apply(this);
    }

    @Override
    public IDataset getMainTable() {
        return mainTable;
    }

    @Override
    public IDataset getSecondTable() {
        return secondTable;
    }

    @Override
    public JoinMode getMode() {
        return mode;
    }

    @Override
    public On getOn() {
        return on;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.BLANK).append(this.mode.getSql());
        getSecondTable().sql(module, this, context, sqlBuilder);
        getOn().sql(module, this, context, sqlBuilder);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.mainTable, this.secondTable, this.on);
    }
}
