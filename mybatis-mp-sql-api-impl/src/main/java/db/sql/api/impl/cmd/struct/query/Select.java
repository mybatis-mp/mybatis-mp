package db.sql.api.impl.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.Distinct;
import db.sql.api.cmd.struct.query.ISelect;
import db.sql.api.impl.cmd.dbFun.Count;
import db.sql.api.impl.cmd.dbFun.Function;
import db.sql.api.impl.tookit.Lists;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.LinkedList;
import java.util.List;

public class Select implements ISelect<Select> {

    private final List<Cmd> selectFields = new LinkedList<>();
    private boolean distinct = false;

    @Override
    public List<Cmd> getSelectField() {
        return selectFields;
    }

    @Override
    public Select selectIgnore(Cmd column) {
        selectFields.remove(column);
        return this;
    }

    @Override
    public Select distinct() {
        this.distinct = true;
        return this;
    }

    @Override
    public boolean isDistinct() {
        return this.distinct;
    }

    @Override
    public Select select(Cmd field) {
        selectFields.add(field);
        return this;
    }

    @Override
    public Select select(Cmd... fields) {
        Lists.merge(this.selectFields, fields);
        return this;
    }

    @Override
    public Select select(List<Cmd> fields) {
        this.selectFields.addAll(fields);
        return this;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (!(parent instanceof Function)) {
            sqlBuilder.append(SqlConst.SELECT);
        }
        if (distinct) {
            Distinct.INSTANCE.sql(module, this, context, sqlBuilder);
            if ((context.getDbType() == DbType.PGSQL || context.getDbType() == DbType.H2) && parent instanceof Count) {
                sqlBuilder.append(SqlConst.BRACKET_LEFT);
            }
        }
        CmdUtils.join(this, this, context, sqlBuilder, this.getSelectField(), SqlConst.DELIMITER);
        if (distinct) {
            if ((context.getDbType() == DbType.PGSQL || context.getDbType() == DbType.H2) && parent instanceof Count) {
                sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.selectFields);
    }
}
