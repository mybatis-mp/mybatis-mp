package db.sql.api.impl.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.insert.IInsertTable;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractInsert;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.List;

public class InsertTable implements IInsertTable<Table> {

    protected final Table table;

    private boolean insertIgnore = false;

    public InsertTable(Table table) {
        this.table = table;
    }

    public InsertTable(Table table, boolean insertIgnore) {
        this.table = table;
        this.insertIgnore = insertIgnore;
    }

    @Override
    public Table getTable() {
        return table;
    }

    public boolean isInsertIgnore() {
        return insertIgnore;
    }

    public void setInsertIgnore(boolean insertIgnore) {
        this.insertIgnore = insertIgnore;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {

        if (context.getDbType() == DbType.ORACLE && parent instanceof AbstractInsert) {
            AbstractInsert abstractInsert = (AbstractInsert) parent;
            List<List<Cmd>> insertValuesList = null;
            if (Objects.nonNull(abstractInsert.getInsertValues())) {
                insertValuesList = abstractInsert.getInsertValues().getValues();
            }
            if (Objects.nonNull(insertValuesList) && insertValuesList.size() > 1) {
                sqlBuilder.append(" INSERT ALL ");
                return sqlBuilder;
            }
        }
        sqlBuilder.append(insertIgnore && context.getDbType() == DbType.MYSQL ? SqlConst.INSERT_IGNORE_INTO : SqlConst.INSERT_INTO);
        sqlBuilder.append(this.table.getName());
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.table);
    }
}
