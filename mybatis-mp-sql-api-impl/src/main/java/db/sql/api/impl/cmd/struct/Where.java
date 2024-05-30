package db.sql.api.impl.cmd.struct;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.struct.IWhere;
import db.sql.api.impl.cmd.ConditionFactory;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.Objects;
import java.util.function.Function;

public class Where<WHERE extends Where<WHERE>> implements IWhere<WHERE, TableField, Cmd, Object, ConditionChain> {

    private final ConditionFactory conditionFactory;

    private ConditionChain conditionChain;

    private ConditionChain extConditionChain;

    public Where(ConditionFactory conditionFactory) {
        this.conditionFactory = conditionFactory;
    }

    public boolean hasContent() {
        return Objects.nonNull(conditionChain) && conditionChain.hasContent();
    }

    @Override
    public ConditionChain conditionChain() {
        if (this.conditionChain == null) {
            this.conditionChain = new ConditionChain(conditionFactory);
        }
        return conditionChain;
    }

    public ConditionChain extConditionChain() {
        if (this.extConditionChain == null) {
            this.extConditionChain = new ConditionChain(conditionFactory);
        }
        return extConditionChain;
    }

    public ConditionFactory getConditionFactory() {
        return conditionFactory;
    }

    public ConditionChain getConditionChain() {
        return conditionChain;
    }

    @Override
    public <T> WHERE and(Getter<T> column, int storey, Function<TableField, ICondition> function) {
        conditionChain().and(column, storey, function);
        return (WHERE) this;
    }

    @Override
    public <T> WHERE or(Getter<T> column, int storey, Function<TableField, ICondition> function) {
        conditionChain().or(column, storey, function);
        return (WHERE) this;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if ((extConditionChain == null || !extConditionChain.hasContent()) && (this.conditionChain == null || !conditionChain.hasContent())) {
            return sqlBuilder;
        }
        sqlBuilder.append(SqlConst.WHERE);

        if (extConditionChain != null && extConditionChain.hasContent() && this.conditionChain != null && conditionChain.hasContent()) {
            //2的 ConditionChain 都不为空 分别一括号包裹
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.extConditionChain.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            sqlBuilder.append(SqlConst.AND);
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.conditionChain.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);

        } else if (extConditionChain != null && extConditionChain.hasContent()) {
            this.extConditionChain.sql(module, this, context, sqlBuilder);
        } else {
            this.conditionChain.sql(module, this, context, sqlBuilder);
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.conditionChain) || CmdUtils.contain(cmd, this.extConditionChain);
    }
}
