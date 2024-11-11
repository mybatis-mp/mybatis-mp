package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.util.ForeignKeyUtil;
import cn.mybatis.mp.core.sql.util.SelectClassUtil;
import cn.mybatis.mp.core.tenant.TenantUtil;
import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.impl.cmd.executor.AbstractQuery;
import db.sql.api.impl.cmd.struct.On;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.OptimizeOptions;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BaseQuery<Q extends BaseQuery<Q, E>, E> extends AbstractQuery<Q, MybatisCmdFactory> {

    protected final OptimizeOptions optimizeOptions = new OptimizeOptions();
    protected Class returnType;
    protected Consumer<E> onRowEvent;

    public BaseQuery() {
        this(new MybatisCmdFactory());
    }

    public BaseQuery(MybatisCmdFactory mybatisCmdFactory) {
        super(mybatisCmdFactory);
    }

    public BaseQuery(Where where) {
        super(where);
    }

    public Q optimizeOptions(Consumer<OptimizeOptions> consumer) {
        consumer.accept(this.optimizeOptions);
        return (Q) this;
    }

    public OptimizeOptions getOptimizeOptions() {
        return optimizeOptions;
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

    public Class getReturnType() {
        return returnType;
    }

    public <E2, Q2 extends BaseQuery<Q2, E2>> BaseQuery<Q2, E2> setReturnType(Class<E2> returnType) {
        this.returnType = returnType;
        return (BaseQuery<Q2, E2>) this;
    }

    public <E2, Q2 extends BaseQuery<Q2, E2>> BaseQuery<Q2, E2> setReturnType(Class<E2> returnType, Consumer<E2> consumer) {
        return (BaseQuery<Q2, E2>) this.setReturnType(returnType).onRowEvent(consumer);
    }

    public Q onRowEvent(Consumer<E> consumer) {
        this.onRowEvent = consumer;
        return (Q) this;
    }

    public Consumer<E> getOnRowEvent() {
        return onRowEvent;
    }

    @Override
    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        super.initCmdSorts(cmdSorts);
        cmdSorts.put(cn.mybatis.mp.core.sql.executor.Where.class, cmdSorts.get(Where.class));
    }

    @Override
    public Q select(Class entity, int storey) {
        SelectClassUtil.select(this, entity, storey);
        return (Q) this;
    }

    /**************以下为去除警告************/

    @Override
    @SafeVarargs
    public final Q select(int storey, Class... entities) {
        SelectClassUtil.select(this, storey, entities);
        return (Q) this;
    }

    @Override
    @SafeVarargs
    public final Q select(Class... entities) {
        return super.select(entities);
    }

    @Override
    @SafeVarargs
    public final Q select(Cmd... cmds) {
        return super.select(cmds);
    }

    @Override
    @SafeVarargs
    public final <T> Q select(int storey, Getter<T>... columns) {
        return super.select(storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q select(Getter<T>... columns) {
        return super.select(1, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q select(boolean when, Getter<T>... columns) {
        return super.select(when, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q select(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.select(dataset, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q select(boolean when, int storey, Getter<T>... columns) {
        return super.select(when, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q select(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.select(when, dataset, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q selectIgnore(Getter<T>... columns) {
        return super.selectIgnore(columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q selectIgnore(int storey, Getter<T>... columns) {
        return super.selectIgnore(storey, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q from(IDataset<DATASET, DATASET_FIELD>... tables) {
        return super.from(tables);
    }

    @Override
    @SafeVarargs
    public final Q from(Class... entities) {
        return super.from(entities);
    }

    @Override
    @SafeVarargs
    public final Q from(int storey, Class... entities) {
        return super.from(storey, entities);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupBy(Getter<T>... columns) {
        return super.groupBy(columns);
    }

    @Override
    @SafeVarargs
    public final Q groupBy(Cmd... cmds) {
        return super.groupBy(cmds);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupBy(int storey, Getter<T>... columns) {
        return super.groupBy(storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupBy(boolean when, Getter<T>... columns) {
        return super.groupBy(when, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.groupBy(dataset, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupBy(boolean when, int storey, Getter<T>... columns) {
        return super.groupBy(when, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.groupBy(when, dataset, columns);
    }

    @Override
    @SafeVarargs
    public final Q orderBy(Cmd... cmds) {
        return super.orderBy(cmds);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(Getter<T>... columns) {
        return super.orderBy(columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(int storey, Getter<T>... columns) {
        return super.orderBy(storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(boolean when, Getter<T>... columns) {
        return super.orderBy(when, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.orderBy(dataset, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.orderBy(when, dataset, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(boolean when, int storey, Getter<T>... columns) {
        return super.orderBy(when, storey, columns);
    }

    @Override
    @SafeVarargs
    public final Q orderBy(IOrderByDirection orderByDirection, Cmd... cmds) {
        return super.orderBy(orderByDirection, cmds);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(IOrderByDirection orderByDirection, Getter<T>... columns) {
        return super.orderBy(orderByDirection, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(IOrderByDirection orderByDirection, int storey, Getter<T>... columns) {
        return super.orderBy(orderByDirection, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(boolean when, IOrderByDirection orderByDirection, Getter<T>... columns) {
        return super.orderBy(when, orderByDirection, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T>... columns) {
        return super.orderBy(dataset, orderByDirection, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderBy(boolean when, IOrderByDirection orderByDirection, int storey, Getter<T>... columns) {
        return super.orderBy(when, orderByDirection, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T>... columns) {
        return super.orderBy(when, dataset, orderByDirection, columns);
    }

    @Override
    @SafeVarargs
    public final Q orderByDesc(Cmd... cmds) {
        return super.orderByDesc(cmds);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDesc(Getter<T>... columns) {
        return super.orderByDesc(columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDesc(int storey, Getter<T>... columns) {
        return super.orderByDesc(storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDesc(boolean when, Getter<T>... columns) {
        return super.orderByDesc(when, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderByDesc(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.orderByDesc(dataset, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDesc(boolean when, int storey, Getter<T>... columns) {
        return super.orderByDesc(when, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderByDesc(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.orderByDesc(when, dataset, columns);
    }

    /**************以上为去除警告************/
}

