package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.util.ForeignKeyUtil;
import cn.mybatis.mp.core.sql.util.SelectClassUtil;
import cn.mybatis.mp.core.tenant.TenantUtil;
import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.executor.AbstractSubQuery;
import db.sql.api.impl.cmd.struct.On;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseSubQuery<Q extends BaseSubQuery<Q>> extends AbstractSubQuery<Q, MybatisCmdFactory> implements db.sql.api.cmd.basic.IDataset<Q, DatasetField> {

    private final String alias;

    public BaseSubQuery(String alias) {
        super(new MybatisCmdFactory("st"));
        this.alias = alias;
    }

    public BaseSubQuery(String alias, Where where) {
        super(where);
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
    public final Q select(Class entity, int storey) {
        SelectClassUtil.select(this, entity, storey);
        return (Q) this;
    }

    @Override
    @SafeVarargs
    public final Q select(int storey, Class... entities) {
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

    /**************以下为去除警告************/


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
    public final Q selectWithFun(Function<TableField[], Cmd> f, GetterField... getterFields) {
        return super.selectWithFun(f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q selectWithFun(Function<TableField[], Cmd> f, Getter<T>... columns) {
        return super.selectWithFun(f, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q selectWithFun(Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return super.selectWithFun(f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q selectWithFun(boolean when, Function<TableField[], Cmd> f, Getter<T>... columns) {
        return super.selectWithFun(when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return super.selectWithFun(dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return super.selectWithFun(dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final Q selectWithFun(boolean when, Function<TableField[], Cmd> f, GetterField... getterFields) {
        return super.selectWithFun(when, f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q selectWithFun(boolean when, Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return super.selectWithFun(when, f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q selectWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return super.selectWithFun(when, dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q selectWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return super.selectWithFun(when, dataset, f, columnFields);
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
    public final <T> Q groupByWithFun(Function<TableField[], Cmd> f, Getter<T>... columns) {
        return super.groupByWithFun(f, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        return super.groupBy(when, dataset, columns);
    }

    @Override
    @SafeVarargs
    public final Q groupByWithFun(Function<TableField[], Cmd> f, GetterField... getterFields) {
        return super.groupByWithFun(f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupByWithFun(Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return super.groupByWithFun(f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupByWithFun(boolean when, Function<TableField[], Cmd> f, Getter<T>... columns) {
        return super.groupByWithFun(when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return super.groupByWithFun(dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return super.groupByWithFun(dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final Q groupByWithFun(boolean when, Function<TableField[], Cmd> f, GetterField... getterFields) {
        return super.groupByWithFun(when, f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q groupByWithFun(boolean when, Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return super.groupByWithFun(when, f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return super.groupByWithFun(when, dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q groupByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return super.groupByWithFun(when, dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q having(Function<TableField[], ICondition> f, Getter<T>... columns) {
        return super.having(f, columns);
    }

    @Override
    @SafeVarargs
    public final Q having(Function<TableField[], ICondition> f, GetterField... getterFields) {
        return super.having(f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q having(Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        return super.having(f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q having(boolean when, Function<TableField[], ICondition> f, Getter<T>... columns) {
        return super.having(when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q having(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return super.having(dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q having(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return super.having(dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final Q having(boolean when, Function<TableField[], ICondition> f, GetterField... getterFields) {
        return super.having(when, f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q having(boolean when, Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        return super.having(when, f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q having(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return super.having(dataset, when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingAnd(Function<TableField[], ICondition> f, Getter<T>... columns) {
        return super.havingAnd(f, columns);
    }

    @Override
    @SafeVarargs
    public final Q havingAnd(Function<TableField[], ICondition> f, GetterField... getterFields) {
        return super.havingAnd(f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingAnd(Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        return super.havingAnd(f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingAnd(boolean when, Function<TableField[], ICondition> f, Getter<T>... columns) {
        return super.havingAnd(when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return super.havingAnd(dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return super.havingAnd(dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final Q havingAnd(boolean when, Function<TableField[], ICondition> f, GetterField... getterFields) {
        return super.havingAnd(when, f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingAnd(boolean when, Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        return super.havingAnd(when, f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return super.havingAnd(dataset, when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return super.havingAnd(dataset, when, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingOr(Function<TableField[], ICondition> f, Getter<T>... columns) {
        return super.havingOr(f, columns);
    }

    @Override
    @SafeVarargs
    public final Q havingOr(Function<TableField[], ICondition> f, GetterField... getterFields) {
        return super.havingOr(f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingOr(Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        return super.havingOr(f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingOr(boolean when, Function<TableField[], ICondition> f, Getter<T>... columns) {
        return super.havingOr(when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingOr(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return super.havingOr(dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingOr(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return super.havingOr(dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final Q havingOr(boolean when, Function<TableField[], ICondition> f, GetterField... getterFields) {
        return super.havingOr(when, f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q havingOr(boolean when, Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        return super.havingOr(when, f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return super.havingOr(dataset, when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return super.havingOr(dataset, when, f, columnFields);
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

    @Override
    @SafeVarargs
    public final <T> Q orderByDescWithFun(Function<TableField[], Cmd> f, Getter<T>... columns) {
        return super.orderByDescWithFun(f, columns);
    }

    @Override
    @SafeVarargs
    public final Q orderByDescWithFun(Function<TableField[], Cmd> f, GetterField... getterFields) {
        return super.orderByDescWithFun(f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDescWithFun(Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return super.orderByDescWithFun(f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDescWithFun(boolean when, Function<TableField[], Cmd> f, Getter<T>... columns) {
        return super.orderByDescWithFun(when, f, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderByDescWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return super.orderByDescWithFun(dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderByDescWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return super.orderByDescWithFun(dataset, f, columnFields);
    }

    @Override
    @SafeVarargs
    public final Q orderByDescWithFun(boolean when, Function<TableField[], Cmd> f, GetterField... getterFields) {
        return super.orderByDescWithFun(when, f, getterFields);
    }

    @Override
    @SafeVarargs
    public final <T> Q orderByDescWithFun(boolean when, Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return super.orderByDescWithFun(when, f, storey, columns);
    }

    @Override
    @SafeVarargs
    public final <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderByDescWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return super.orderByDescWithFun(when, dataset, f, columns);
    }

    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> Q orderByDescWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return super.orderByDescWithFun(when, dataset, f, columnFields);
    }

    /**************以上为去除警告************/
}
