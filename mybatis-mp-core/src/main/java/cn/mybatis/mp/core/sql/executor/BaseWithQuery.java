package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.ForeignKeyUtil;
import db.sql.api.Cmd;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractWithQuery;
import db.sql.api.impl.cmd.struct.On;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BaseWithQuery<Q extends BaseWithQuery<Q>> extends AbstractWithQuery<Q, MybatisCmdFactory> implements db.sql.api.cmd.basic.IDataset<Q, DatasetField> {

    private final String name;

    private String alias;

    public BaseWithQuery(String name) {
        super(new MybatisCmdFactory("wt"));
        this.name = name;
    }

    @Override
    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        super.initCmdSorts(cmdSorts);
        cmdSorts.put(Where.class, cmdSorts.get(db.sql.api.impl.cmd.struct.Where.class));
    }

    @Override
    public String getAlias() {
        if (Objects.isNull(alias)) {
            return this.name;
        }
        return alias;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public Table asTable(String alias) {
        return new MpTable(this.getAlias(), alias);
    }

    @Override
    public Q as(String alias) {
        this.alias = alias;
        return (Q) this;
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
    public Consumer<On> joinEntityIntercept(Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<On> consumer) {
        this.addTenantCondition(secondTable, secondTableStorey);
        this.addLogicDeleteCondition(secondTable, secondTableStorey);
        if (Objects.isNull(consumer)) {
            //自动加上外键连接条件
            consumer = ForeignKeyUtil.buildForeignKeyOnConsumer($, mainTable, mainTableStorey, secondTable, secondTableStorey);
        }
        return consumer;
    }
}
