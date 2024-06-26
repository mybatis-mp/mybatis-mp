package db.sql.api.impl.cmd.struct;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.IOn;
import db.sql.api.impl.cmd.ConditionFactory;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class On implements IOn<On, Join, Table, TableField, Cmd, Object, ConditionChain> {

    private final ConditionFactory conditionFactory;

    private final Join join;

    private final ConditionChain conditionChain;

    public On(ConditionFactory conditionFactory, Join join) {
        this.conditionFactory = conditionFactory;
        this.join = join;
        conditionChain = new ConditionChain(conditionFactory);
    }

    @Override
    public Join getJoin() {
        return join;
    }

    @Override
    public ConditionChain conditionChain() {
        return conditionChain;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.ON);
        if (!conditionChain.hasContent()) {
            throw new RuntimeException("ON has no on conditions");
        }
        conditionChain().sql(module, this, context, sqlBuilder);
        return sqlBuilder;
    }

    public ConditionFactory getConditionFactory() {
        return conditionFactory;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.conditionChain);
    }
}
