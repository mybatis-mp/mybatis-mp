package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.ForeignKeyUtil;
import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.condition.Exists;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.executor.AbstractSubQuery;
import db.sql.api.impl.cmd.struct.From;
import db.sql.api.impl.cmd.struct.OnDataset;
import db.sql.api.impl.cmd.struct.query.With;
import db.sql.api.impl.tookit.SqlConst;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BaseSubQuery<Q extends BaseSubQuery<Q>> extends AbstractSubQuery<Q, MybatisCmdFactory> implements Dataset<Q, DatasetField> {
    private final String alias;

    public BaseSubQuery(String alias) {
        super(new MybatisCmdFactory("st"));
        this.alias = alias;
    }

    @Override
    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        super.initCmdSorts(cmdSorts);
        cmdSorts.put(cn.mybatis.mp.core.sql.executor.Where.class, cmdSorts.get(db.sql.api.impl.cmd.struct.Where.class));
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Q as(String alias) {
        throw new RuntimeException("not support");
    }

    @Override
    public Q select(Class entity, int storey) {
        SelectClassUtil.select(this, entity, storey);
        return (Q) this;
    }

    @Override
    public Q select(int storey, Class... entities) {
        SelectClassUtil.select(this, storey, entities);
        return (Q) this;
    }

    protected void addTenantCondition(Class entity, int storey) {
        TenantUtil.addTenantCondition(this.$where(), $, entity, storey);
    }

    protected void addLogicDeleteCondition(Class entity, int storey) {
        LogicDeleteUtil.addLogicDeleteCondition(this.$where(), $, entity, storey);
    }

    @Override
    public void fromEntityIntercept(Class entity, int storey) {
        this.addTenantCondition(entity, storey);
        this.addLogicDeleteCondition(entity, storey);
    }

    @Override
    public Consumer<OnDataset> joinEntityIntercept(Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<OnDataset> consumer) {
        this.addTenantCondition(secondTable, secondTableStorey);
        this.addLogicDeleteCondition(secondTable, secondTableStorey);
        if (Objects.isNull(consumer)) {
            //自动加上外键连接条件
            consumer = ForeignKeyUtil.buildForeignKeyOnConsumer($, mainTable, mainTableStorey, secondTable, secondTableStorey);
        }
        return consumer;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (parent instanceof From) {
            return sqlBuilder.append(SqlConst.BLANK).append(this.alias);
        }

        if (parent instanceof In || parent instanceof Exists || parent instanceof With) {
            return super.sql(module, this, context, sqlBuilder);
        }
        sqlBuilder.append(SqlConst.BRACKET_LEFT);
        super.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        if (this.alias != null) {
            sqlBuilder.append(SqlConst.AS(context.getDbType())).append(this.alias);
        }

        return sqlBuilder;
    }
}
