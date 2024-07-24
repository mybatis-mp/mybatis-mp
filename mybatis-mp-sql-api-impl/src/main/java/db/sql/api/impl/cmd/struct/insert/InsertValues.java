package db.sql.api.impl.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.insert.IInsertValues;
import db.sql.api.impl.cmd.executor.AbstractInsert;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.ArrayList;
import java.util.List;

public class InsertValues implements IInsertValues<Cmd> {

    protected List<List<Cmd>> values;

    @Override
    public List<List<Cmd>> getValues() {
        return values;
    }

    public InsertValues add(List<Cmd> values) {
        if (this.values == null) {
            this.values = new ArrayList<>();
        }
        this.values.add(values);
        return this;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        boolean oracleMuti = false;
        AbstractInsert abstractInsert = null;
        if (context.getDbType() == DbType.ORACLE && parent instanceof AbstractInsert) {
            List<List<Cmd>> insertValues = this.getValues();
            if (Objects.nonNull(insertValues) && insertValues.size() > 1) {
                abstractInsert = (AbstractInsert) parent;
                oracleMuti = true;
            }
        }

        if (!oracleMuti) {
            sqlBuilder.append(SqlConst.VALUES);
        }

        boolean isFirstLine = true;
        for (List<Cmd> values : this.values) {

            if (oracleMuti) {
                sqlBuilder.append(SqlConst.INTO);
                sqlBuilder.append(abstractInsert.getInsertTable().getTable().getName(context.getDbType()));
                abstractInsert.getInsertFields().sql(module, this, context, sqlBuilder);
                sqlBuilder.append(SqlConst.VALUES);
            }

            if (!isFirstLine && !oracleMuti) {
                sqlBuilder.append(SqlConst.DELIMITER);
            }
            sqlBuilder.append(SqlConst.BLANK).append(SqlConst.BRACKET_LEFT);
            boolean isFirst = true;
            for (Cmd value : values) {
                if (!isFirst) {
                    sqlBuilder.append(SqlConst.DELIMITER);
                }
                value.sql(module, this, context, sqlBuilder);
                isFirst = false;
            }
            sqlBuilder.append(SqlConst.BRACKET_RIGHT).append(SqlConst.BLANK);
            isFirstLine = false;
        }

        if (oracleMuti) {
            sqlBuilder.append(SqlConst.SELF_FROM_DUAL);
        }

        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.values);
    }
}
