package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.util.ForeignKeyUtil;
import cn.mybatis.mp.core.tenant.TenantUtil;
import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractUpdate;
import db.sql.api.impl.cmd.struct.On;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BaseUpdate<T extends BaseUpdate<T>> extends AbstractUpdate<T, MybatisCmdFactory> {

    public BaseUpdate() {
        super(new MybatisCmdFactory());
    }

    public BaseUpdate(Where where) {
        super(where);
    }

    @Override
    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        super.initCmdSorts(cmdSorts);
        cmdSorts.put(cn.mybatis.mp.core.sql.executor.Where.class, cmdSorts.get(Where.class));
    }


    protected void addTenantCondition(Class entity, int storey) {
        TenantUtil.addTenantCondition(this.$where(), $, entity, storey);
    }

    protected void addLogicDeleteCondition(Class entity, int storey) {
        LogicDeleteUtil.addLogicDeleteCondition(this.$where(), $, entity, storey);
    }

    @Override
    public void updateEntityIntercept(Class entity) {
        this.addTenantCondition(entity, 1);
        this.addLogicDeleteCondition(entity, 1);
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
    public final T update(Table... tables) {
        return super.update(tables);
    }

    @Override
    @SafeVarargs
    public final T update(Class... entities) {
        return super.update(entities);
    }


    @Override
    @SafeVarargs
    public final <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> T from(IDataset<DATASET, DATASET_FIELD>... tables) {
        return super.from(tables);
    }

    @Override
    @SafeVarargs
    public final T from(Class... entities) {
        return super.from(entities);
    }

    @Override
    @SafeVarargs
    public final T from(int storey, Class... entities) {
        return super.from(storey, entities);
    }

    /**************以上为去除警告************/
}
