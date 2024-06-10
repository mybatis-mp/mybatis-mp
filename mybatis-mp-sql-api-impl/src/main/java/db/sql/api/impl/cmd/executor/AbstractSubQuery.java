package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.ISubQuery;
import db.sql.api.cmd.struct.Joins;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.condition.Exists;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.struct.*;
import db.sql.api.impl.cmd.struct.query.*;
import db.sql.api.impl.tookit.SqlConst;

public abstract class AbstractSubQuery<SELF extends AbstractSubQuery<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory> extends AbstractQuery<SELF, CMD_FACTORY>
        implements ISubQuery<SELF,
        Table,
        Dataset,
        TableField,
        DatasetField,
        Cmd,
        Object,
        CMD_FACTORY,
        ConditionChain,
        With,
        Select,
        FromDataset,
        JoinDataset,
        OnDataset,
        Joins<JoinDataset>,
        Where,
        GroupBy,
        Having,
        OrderBy,
        Limit,
        ForUpdate,
        Union
        > {
    public AbstractSubQuery(CMD_FACTORY $) {
        super($);
    }

    public abstract String getAlias();


    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (parent instanceof From) {
            return sqlBuilder.append(SqlConst.BLANK).append(this.getAlias());
        }

        if (parent instanceof In || parent instanceof Exists || parent instanceof With) {
            return super.sql(module, this, context, sqlBuilder);
        }
        sqlBuilder.append(SqlConst.BRACKET_LEFT);
        super.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        if (this.getAlias() != null) {
            sqlBuilder.append(SqlConst.AS(context.getDbType())).append(this.getAlias());
        }

        return sqlBuilder;
    }

}

