package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.executor.IUpdate;
import db.sql.api.cmd.struct.Joins;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.ConditionFactory;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.struct.*;
import db.sql.api.impl.cmd.struct.update.UpdateSets;
import db.sql.api.impl.cmd.struct.update.UpdateTable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractUpdate<SELF extends AbstractUpdate<SELF, CMD_FACTORY>,
        CMD_FACTORY extends CmdFactory
        >
        extends BaseExecutor<SELF, CMD_FACTORY>
        implements IUpdate<SELF,
        Table,
        Dataset,
        TableField,
        DatasetField,
        Cmd,
        Object,
        ConditionChain,
        UpdateTable,
        FromTable,
        JoinTable,
        OnTable,
        Where
        > {

    protected final ConditionFactory conditionFactory;
    protected final CMD_FACTORY $;
    protected UpdateTable updateTable;
    protected FromTable from;
    protected UpdateSets updateSets;
    protected Where where;
    protected Joins joins;

    public AbstractUpdate(CMD_FACTORY $) {
        this.$ = $;
        this.conditionFactory = new ConditionFactory($);
    }

    public AbstractUpdate(Where where) {
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

    @Override
    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        int i = 0;
        cmdSorts.put(UpdateTable.class, i += 10);
        cmdSorts.put(UpdateSets.class, i += 10);
        cmdSorts.put(FromTable.class, i += 10);
        cmdSorts.put(Joins.class, i += 10);
        cmdSorts.put(Where.class, i += 10);
    }

    @Override
    public UpdateTable $update(Table... tables) {
        if (this.updateTable == null) {
            this.updateTable = new UpdateTable(tables);
            this.append(this.updateTable);
        }
        return this.updateTable;
    }

    @Override
    public SELF update(Class entity, Consumer<Table> consumer) {
        this.updateEntityIntercept(entity);
        Table table = this.$.table(entity);
        this.update(table);
        return (SELF) this;
    }

    @Override
    public SELF update(Class... entities) {
        Table[] tables = new Table[entities.length];
        for (int i = 0; i < entities.length; i++) {
            Class entity = entities[i];
            this.updateEntityIntercept(entity);
            tables[i] = $.table(entity);
        }
        return this.update(tables);
    }

    @Override
    public SELF set(Cmd field, Object value) {
        Cmd v = Methods.convert(value);
        if (this.updateSets == null) {
            this.updateSets = new UpdateSets();
            this.append(this.updateSets);
        }
        this.updateSets.set((TableField) field, v);
        return (SELF) this;
    }

    public <T> SELF set(boolean when, Getter<T> field, Object value) {
        if (!when) {
            return (SELF) this;
        }
        return this.set(field, value);
    }

    public <T, V> SELF set(Getter<T> field, V value, Predicate<V> predicate) {
        return this.set(predicate.test(value), field, value);
    }

    @Override
    public <T> SELF set(Getter<T> field, Object value) {
        return this.set($.field(field), value);
    }

    @Override
    public <T> SELF set(Getter<T> field, Function<TableField, Cmd> function) {
        TableField tableField = $.field(field);
        return this.set(tableField, function.apply(tableField));
    }

    @Override
    public JoinTable $join(JoinMode mode, Table mainTable, Table secondTable) {
        JoinTable join = new JoinTable(mode, mainTable, secondTable, this::apply);
        if (Objects.isNull(joins)) {
            joins = new Joins();
            this.append(joins);
        }
        joins.add(join);
        return join;
    }

    @Override
    public SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<OnTable> consumer) {
        consumer = this.joinEntityIntercept(mainTable, mainTableStorey, secondTable, secondTableStorey, consumer);
        return this.join(mode, this.$.table(mainTable, mainTableStorey), this.$.table(secondTable, secondTableStorey), consumer);
    }

    @Override
    public SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Table secondTable, Consumer<OnTable> consumer) {
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
    public <T> SELF and(Getter<T> column, int storey, Function<TableField, ICondition> function) {
        $where().and(column, storey, function);
        return (SELF) this;
    }

    @Override
    public <T> SELF or(Getter<T> column, int storey, Function<TableField, ICondition> function) {
        $where().or(column, storey, function);
        return (SELF) this;
    }

    @Override
    public FromTable $from(Dataset... tables) {
        if (this.from == null) {
            from = new FromTable();
            this.append(from);
        }
        this.from.append(tables);
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
    public SELF join(JoinMode mode, Table mainTable, Table secondTable, Consumer<OnTable> consumer) {
        JoinTable join = $join(mode, mainTable, secondTable);
        if (consumer != null) {
            consumer.accept(join.getOn());
        }
        return (SELF) this;
    }

    public UpdateTable getUpdateTable() {
        return this.updateTable;
    }

    public UpdateSets getUpdateSets() {
        return this.updateSets;
    }

    public Joins getJoins() {
        return this.joins;
    }

    public Where getWhere() {
        return this.where;
    }

    public FromTable getFrom() {
        return from;
    }

    private OnTable apply(JoinTable joinTable) {
        return new OnTable(this.conditionFactory, joinTable);
    }


    @Override
    public StringBuilder sql(SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (Objects.nonNull(this.getJoins())) {
            if (context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB || context.getDbType() == DbType.DM) {
                // mysql dm 类数据库 update join 是在 update table 之后的
                this.cmdSorts().remove(Joins.class);
                this.cmdSorts().put(Joins.class, this.cmdSorts().get(UpdateTable.class) + 1);
            }
        }
        return super.sql(context, sqlBuilder);
    }
}
