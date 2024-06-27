package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.executor.ISubQuery;
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

public abstract class AbstractSubQuery<SELF extends AbstractSubQuery<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory>
        extends AbstractQuery<SELF, CMD_FACTORY>

        implements ISubQuery<SELF,
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

    protected String alias;

    public AbstractSubQuery(CMD_FACTORY $) {
        super($);
    }

    public AbstractSubQuery(Where where) {
        super(where);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public DatasetField $(String columnName) {
        return new DatasetField(this, columnName);
    }

    @Override
    public <E, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD $(IDataset<DATASET, DATASET_FIELD> dataset, Getter<E> getter) {
        return super.$(dataset, getter);
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD $(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return super.$(dataset, columnName);
    }

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

