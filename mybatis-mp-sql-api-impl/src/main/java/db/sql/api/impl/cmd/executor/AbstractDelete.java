package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.executor.IDelete;
import db.sql.api.cmd.struct.Joins;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.ConditionFactory;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.struct.*;
import db.sql.api.impl.cmd.struct.delete.DeleteTable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractDelete<SELF extends AbstractDelete<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory>
        extends BaseExecutor<SELF, CMD_FACTORY>
        implements IDelete<SELF,
        Table,
        TableField,
        Cmd,
        Object,
        ConditionChain,
        DeleteTable,
        From, Join, On, Where> {

    protected final ConditionFactory conditionFactory;
    protected final CMD_FACTORY $;
    protected DeleteTable deleteTable;
    protected From from;
    protected Where where;
    protected Joins joins;

    public AbstractDelete(CMD_FACTORY $) {
        this.$ = $;
        this.conditionFactory = new ConditionFactory($);
    }

    public AbstractDelete(Where where) {
        this.$ = (CMD_FACTORY) where.getConditionFactory().getCmdFactory();
        this.conditionFactory = where.getConditionFactory();
        this.where = where;
        this.append(where);
    }

    public <T> TableField $(Getter<T> getter) {
        return this.$(getter, 1);
    }

    public <T> TableField $(Getter<T> getter, int storey) {
        return $().field(getter, storey);
    }

    public Table $(Class entityType) {
        return this.$(entityType, 1);
    }

    public Table $(Class entityType, int storey) {
        return $().table(entityType, storey);
    }

    public TableField $(Class entityType, String fieldName) {
        return this.$(entityType, fieldName, 1);
    }

    public TableField $(Class entityType, String fieldName, int storey) {
        return $().field(entityType, fieldName, storey);
    }

    @Override
    public CMD_FACTORY $() {
        return $;
    }

    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        int i = 0;
        cmdSorts.put(DeleteTable.class, i += 10);
        cmdSorts.put(From.class, i += 10);
        cmdSorts.put(Joins.class, i += 10);
        cmdSorts.put(Where.class, i += 10);
    }

    @Override
    public DeleteTable $delete(IDataset... tables) {
        if (this.deleteTable == null) {
            this.deleteTable = new DeleteTable(tables);
        }
        this.append(this.deleteTable);
        return this.deleteTable;
    }

    @Override
    public SELF delete(Class... entities) {
        int length = entities.length;
        Table[] tables = new Table[length];
        for (int i = 0; i < length; i++) {
            Class entity = entities[i];
            tables[i] = $.table(entity, 1);
        }
        return this.delete(tables);
    }

    @Override
    public From $from(IDataset table) {
        if (this.from == null) {
            from = new From();
            this.append(from);
        }
        this.from.append(table);
        return from;
    }

    @Override
    public SELF from(Class entity, int storey, Consumer<Table> consumer) {
        this.fromEntityIntercept(entity, storey);
        Table table = this.$.table(entity, storey);
        this.from(table);
        if (Objects.nonNull(consumer)) {
            consumer.accept(table);
        }
        return (SELF) this;
    }

    @Override
    public Join $join(JoinMode mode, IDataset mainTable, IDataset secondTable) {
        Join join = new Join(mode, mainTable, secondTable, (joinTable) -> new On(conditionFactory, joinTable));
        if (Objects.isNull(joins)) {
            joins = new Joins();
            this.append(joins);
        }
        joins.add(join);
        return join;
    }

    @Override
    public SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<On> consumer) {
        consumer = this.joinEntityIntercept(mainTable, mainTableStorey, secondTable, secondTableStorey, consumer);
        return this.join(mode, this.$.table(mainTable, mainTableStorey), this.$.table(secondTable, secondTableStorey), consumer);
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(JoinMode mode, Class mainTable, int mainTableStorey, DATASET secondTable, Consumer<On> consumer) {
        return this.join(mode, this.$.table(mainTable, mainTableStorey), secondTable, consumer);
    }

    @Override
    public Where $where() {
        if (where == null) {
            where = new Where(this.conditionFactory);
            this.append(where);
        }
        return where;
    }

    @Override
    public <T> SELF and(Getter<T> column, int storey, Function<TableField, ICondition> f) {
        $where().and(column, storey, f);
        return (SELF) this;
    }

    @Override
    public <T> SELF or(Getter<T> column, int storey, Function<TableField, ICondition> f) {
        $where().or(column, storey, f);
        return (SELF) this;
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>, DATASET2 extends IDataset<DATASET2, DATASET_FIELD2>, DATASET_FIELD2 extends IDatasetField<DATASET_FIELD2>> SELF join(JoinMode mode, DATASET mainTable, DATASET2 secondTable, Consumer<On> consumer) {
        Join join = $join(mode, mainTable, secondTable);
        consumer.accept(join.getOn());
        return (SELF) this;
    }

    public DeleteTable getDeleteTable() {
        return deleteTable;
    }

    public From getFrom() {
        return from;
    }

    public Joins getJoins() {
        return joins;
    }

    public Where getWhere() {
        return where;
    }

}
