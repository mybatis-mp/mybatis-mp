package db.sql.api.impl.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.insert.IInsertFields;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.executor.AbstractInsert;
import db.sql.api.impl.tookit.Lists;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.ArrayList;
import java.util.List;

public class InsertFields implements IInsertFields<TableField> {

    //SQL SERVER 专用
    private String output;

    protected List<TableField> tableFields;

    public InsertFields field(TableField field) {
        if (tableFields == null) {
            this.tableFields = new ArrayList<>(10);
        }
        this.tableFields.add(field);
        return this;
    }

    public InsertFields field(TableField... fields) {
        if (tableFields == null) {
            this.tableFields = new ArrayList<>(10);
        }
        Lists.merge(this.tableFields, fields);
        return this;
    }

    public InsertFields field(List<TableField> fields) {
        if (tableFields == null) {
            this.tableFields = new ArrayList<>(10);
        }
        this.tableFields.addAll(fields);
        return this;
    }

    @Override
    public List<TableField> getFields() {
        return this.tableFields;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (context.getDbType() == DbType.ORACLE && parent instanceof AbstractInsert) {
            AbstractInsert abstractInsert = (AbstractInsert) parent;

            List<List<Cmd>> insertValues = null;
            if (Objects.nonNull(abstractInsert.getInsertValues())) {
                insertValues = abstractInsert.getInsertValues().getValues();
            }
            if (Objects.nonNull(insertValues) && insertValues.size() > 1) {
                //啥也不做
                return sqlBuilder;
            }
        }

        sqlBuilder.append(SqlConst.BLANK).append(SqlConst.BRACKET_LEFT);
        boolean isFirst = true;
        for (TableField tableField : this.tableFields) {
            if (!isFirst) {
                sqlBuilder.append(SqlConst.DELIMITER);
            }
            sqlBuilder.append(tableField.getName(context.getDbType()));
            isFirst = false;
        }
        sqlBuilder.append(SqlConst.BRACKET_RIGHT).append(SqlConst.BLANK);

        if (context.getDbType() == DbType.SQL_SERVER) {
            if (this.output != null) {
                sqlBuilder.append(SqlConst.BLANK).append(this.output).append(SqlConst.BLANK);
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.tableFields);
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
