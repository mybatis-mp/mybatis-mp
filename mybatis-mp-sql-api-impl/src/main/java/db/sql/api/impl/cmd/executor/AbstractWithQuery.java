package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IWithQuery;
import db.sql.api.cmd.struct.Joins;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.condition.Exists;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.struct.*;
import db.sql.api.impl.cmd.struct.query.*;
import db.sql.api.impl.tookit.SqlConst;

public abstract class AbstractWithQuery<SELF extends AbstractWithQuery<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory> extends AbstractSubQuery<SELF, CMD_FACTORY>
        implements IWithQuery<SELF,
        Table,
        TableField,
        DatasetField,
        Cmd,
        Object,
        CMD_FACTORY,
        ConditionChain,
        With,
        Select,
        From,
        Join,
        On,
        Joins<Join>,
        Where,
        GroupBy,
        Having,
        OrderBy,
        Limit,
        ForUpdate,
        Union
        > {

    protected String name;

    public AbstractWithQuery(CMD_FACTORY $) {
        super($);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Table asTable(String alias) {
        return new Table(this.getName(), alias);
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (parent instanceof In || parent instanceof Exists || parent instanceof With) {
            return super.sql(module, parent, context, sqlBuilder);
        }
        return sqlBuilder.append(SqlConst.BLANK).append(this.getAlias());
    }
}

