package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.ColumnField;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.*;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.cmd.executor.IWithQuery;
import db.sql.api.cmd.struct.Joins;
import db.sql.api.cmd.struct.query.Unions;
import db.sql.api.cmd.struct.query.Withs;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.ConditionFactory;
import db.sql.api.impl.cmd.basic.*;
import db.sql.api.impl.cmd.struct.*;
import db.sql.api.impl.cmd.struct.query.*;
import db.sql.api.impl.tookit.LambdaUtil;
import db.sql.api.impl.tookit.SqlConst;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractQuery<SELF extends AbstractQuery<SELF, CMD_FACTORY>,
        CMD_FACTORY extends CmdFactory>

        extends BaseExecutor<SELF, CMD_FACTORY>
        implements IQuery<SELF,
        Table,
        TableField,
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
        >, Cmd {

    protected final ConditionFactory conditionFactory;

    protected final CMD_FACTORY $;

    protected Select select;

    protected Withs withs;

    protected From from;

    protected Where where;

    protected Joins joins;

    protected GroupBy groupBy;

    protected Having having;

    protected OrderBy orderBy;

    protected Limit limit;

    protected ForUpdate forUpdate;

    protected Unions unions;

    protected Map<String, Consumer<Where>> fetchFilters;

    public AbstractQuery(CMD_FACTORY $) {
        this.$ = $;
        this.conditionFactory = new ConditionFactory($);
    }

    public AbstractQuery(Where where) {
        this.$ = (CMD_FACTORY) where.getConditionFactory().getCmdFactory();
        this.conditionFactory = where.getConditionFactory();
        this.where = where;
        this.append(where);
    }

    @Override
    public CMD_FACTORY $() {
        return $;
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

    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD $(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return this.$().field(dataset, columnName);
    }

    public <E, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD $(IDataset<DATASET, DATASET_FIELD> dataset, Getter<E> getter) {
        return this.$().field(dataset, getter);
    }

    @Override
    public <T> SELF fetchFilter(Getter<T> getter, Consumer<Where> where) {
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(getter);
        String key = lambdaFieldInfo.getType().getName() + "." + lambdaFieldInfo.getName();
        if (Objects.isNull(fetchFilters)) {
            this.fetchFilters = new HashMap<>();
        }
        fetchFilters.put(key, where);
        return (SELF) this;
    }

    @Override
    public Map<String, Consumer<Where>> getFetchFilters() {
        return fetchFilters;
    }

    @Override
    protected void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        int i = 0;
        cmdSorts.put(Withs.class, i += 10);
        cmdSorts.put(Select.class, i += 10);
        cmdSorts.put(From.class, i += 10);
        cmdSorts.put(Joins.class, i += 10);
        cmdSorts.put(Where.class, i += 10);
        cmdSorts.put(GroupBy.class, i += 10);
        cmdSorts.put(Having.class, i += 10);
        cmdSorts.put(OrderBy.class, i += 10);
        cmdSorts.put(Limit.class, i += 10);
        cmdSorts.put(ForUpdate.class, i += 10);
        cmdSorts.put(Unions.class, i += 10);
        cmdSorts.put(UnionsCmdLists.class, i += 10);
    }

    @Override
    public With $with(IWithQuery withQuery) {
        if (Objects.isNull(this.withs)) {
            this.withs = new Withs();
            this.append(this.withs);
        }
        With with = new With(withQuery);
        this.withs.add(with);
        return with;
    }

    @Override
    public Select $select() {
        if (select == null) {
            select = new Select();
            this.append(select);
        }
        return select;
    }

    @Override
    public SELF selectCount1() {
        this.select(Count1.INSTANCE);
        return (SELF) this;
    }

    @Override
    public SELF selectCountAll() {
        this.select(CountAll.INSTANCE);
        return (SELF) this;
    }

    /**
     * select 列
     *
     * @param column 列
     * @param storey 列存储层级
     * @param <T>    列的实体类
     * @return 自己
     */
    @Override
    public <T> SELF select(Getter<T> column, int storey) {
        return this.select($.field(column, storey));
    }


    /**
     * select 子查询 列
     *
     * @param column 列
     * @param storey 列存储层级
     * @param <T>    列的实体类
     * @param f      转换函数
     * @return 自己
     */
    @Override
    public <T> SELF selectWithFun(Getter<T> column, int storey, Function<TableField, Cmd> f) {
        return this.select(f.apply($.field(column, storey)));
    }


    @Override
    public <T> SELF selectWithFun(Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return this.select(f.apply($.fields(storey, columns)));
    }

    @Override
    public SELF selectWithFun(GetterField[] getterFields, Function<TableField[], Cmd> f) {
        return this.select(f.apply($.fields(getterFields)));
    }

    @Override
    public <T> SELF select(int storey, Getter<T>... columns) {
        return this.select($.fields(storey, columns));
    }

    @Override
    public SELF select(String columnName) {
        return this.select($.column(columnName));
    }


    @Override
    public SELF selectWithFun(String columnName, Function<IDatasetField, Cmd> f) {
        return this.select(f.apply($.column(columnName)));
    }

    /**
     * select 子查询 列
     *
     * @param dataset 子查询
     * @param column  列
     * @param <T>     列的实体类
     * @return
     */
    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF select(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        return this.select(this.$(dataset, column));
    }

    /**
     * select 子查询 列
     *
     * @param dataset 子查询
     * @param column  列
     * @param f       转换函数
     * @param <T>     列的实体类
     * @return
     */
    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        return this.select(f.apply(this.$(dataset, column)));
    }

    /**
     * select 子查询 列
     *
     * @param dataset    子查询
     * @param columnName 列
     * @param f          转换函数
     * @return
     */
    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, Cmd> f) {
        return this.select(f.apply(this.$(dataset, columnName)));
    }


    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return this.select(this.apply(dataset, f, columns));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return this.select(this.apply(dataset, f, columnFields));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF select(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return this.select(this.$(dataset, columnName));
    }

    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF select(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        return this.select(f.apply(this.$(dataset, column)));
    }


    @Override
    public From $from(IDataset... tables) {
        if (this.from == null) {
            from = new From();
            this.append(from);
        }
        this.from.append(tables);
        return from;
    }

    @Override
    public SELF from(Class entity, int storey, Consumer<Table> consumer) {
        this.fromEntityIntercept(entity, storey);
        Table table = $.table(entity, storey);
        this.from(table);
        if (Objects.nonNull(consumer)) {
            consumer.accept(table);
        }
        return (SELF) this;
    }

    @Override
    public Join $join(JoinMode mode, IDataset mainTable, IDataset secondTable) {
        Join join = new Join(mode, mainTable, secondTable, (joinDataset -> new On(this.conditionFactory, joinDataset)));
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
        return this.join(mode, $.table(mainTable, mainTableStorey), $.table(secondTable, secondTableStorey), consumer);
    }

    @Override
    public SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, BiConsumer<Table, On> consumer) {
        return this.join(mode, mainTable, mainTableStorey, secondTable, secondTableStorey, (on) -> {
            consumer.accept((Table) on.getJoin().getSecondTable(), on);
        });
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(JoinMode mode, Class mainTable, int mainTableStorey, DATASET secondTable, Consumer<On> consumer) {
        return this.join(mode, $.table(mainTable, mainTableStorey), secondTable, consumer);
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
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>, DATASET2 extends IDataset<DATASET2, DATASET_FIELD2>, DATASET_FIELD2 extends IDatasetField<DATASET_FIELD2>> SELF join(JoinMode mode, DATASET mainTable, DATASET2 secondTable, Consumer<On> consumer) {
        Join join = $join(mode, mainTable, secondTable);
        if (consumer != null) {
            consumer.accept(join.getOn());
        }
        return (SELF) this;
    }

    @Override
    public GroupBy $groupBy() {
        if (groupBy == null) {
            groupBy = new GroupBy();
            this.append(groupBy);
        }
        return groupBy;
    }


    /**
     * groupBy 列
     *
     * @param column 列
     * @param storey 列存储层级
     * @param <T>    列的实体类
     * @return 自己
     */
    @Override
    public <T> SELF groupBy(Getter<T> column, int storey) {
        return this.groupBy($.field(column, storey));
    }


    /**
     * groupBy 子查询 列
     *
     * @param column 列
     * @param storey 列存储层级
     * @param <T>    列的实体类
     * @param f      转换函数
     * @return 自己
     */
    @Override
    public <T> SELF groupByWithFun(Getter<T> column, int storey, Function<TableField, Cmd> f) {
        return this.groupBy(f.apply($.field(column, storey)));
    }


    @Override
    public <T> SELF groupByWithFun(Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return this.groupBy(f.apply($.fields(storey, columns)));
    }

    @Override
    public SELF groupByWithFun(GetterField[] getterFields, Function<TableField[], Cmd> f) {
        return this.groupBy(f.apply($.fields(getterFields)));
    }

    @Override
    public <T> SELF groupBy(int storey, Getter<T>... columns) {
        return this.groupBy($.fields(storey, columns));
    }

    @Override
    public SELF groupBy(String columnName) {
        return this.groupBy($.column(columnName));
    }

    @Override
    public SELF groupByWithFun(String columnName, Function<IDatasetField, Cmd> f) {
        return this.groupBy(f.apply($.column(columnName)));
    }

    /**
     * groupBy 子查询 列
     *
     * @param dataset 子查询
     * @param column  列
     * @param <T>     列的实体类
     * @return
     */
    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        return this.groupBy(this.$(dataset, column));
    }

    /**
     * groupBy 子查询 列
     *
     * @param dataset 子查询
     * @param column  列
     * @param f       转换函数
     * @param <T>     列的实体类
     * @return
     */
    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        return this.groupBy(f.apply(this.$(dataset, column)));
    }

    /**
     * groupBy 子查询 列
     *
     * @param dataset    子查询
     * @param columnName 列
     * @param f          转换函数
     * @return
     */
    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, Cmd> f) {
        return this.groupBy(f.apply(this.$(dataset, columnName)));
    }


    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return this.groupBy(this.apply(dataset, f, columns));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return this.groupBy(this.apply(dataset, f, columnFields));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return this.groupBy(this.$(dataset, columnName));
    }


    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        return this.groupBy(f.apply(this.$(dataset, column)));
    }

    @Override
    public Having $having() {
        if (having == null) {
            having = new Having(this.$);
            this.append(having);
        }
        return having;
    }

    @Override
    public <T> SELF havingAnd(boolean when, Getter<T> column, int storey, Function<TableField, ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(f.apply($.field(column, storey)));
    }

    @Override
    public <T> SELF havingOr(boolean when, Getter<T> column, int storey, Function<TableField, ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(f.apply($.field(column, storey)));
    }

    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(f.apply(this.$(dataset, column)));
    }

    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(f.apply(this.$(dataset, column)));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(f.apply(this.$(dataset, columnName)));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingOr(f.apply(this.$(dataset, columnName)));
    }

    @Override
    public <T> SELF havingAnd(boolean when, Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(f.apply($.fields(storey, columns)));
    }

    @Override
    public <T> SELF havingOr(boolean when, Function<TableField[], ICondition> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(f.apply($.fields(storey, columns)));
    }

    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(this.apply(dataset, f, columns));
    }

    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(this.apply(dataset, f, columns));
    }

    @Override
    public SELF havingAnd(boolean when, GetterField[] getterFields, Function<TableField[], ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(f.apply($.fields(getterFields)));
    }

    @Override
    public SELF havingOr(boolean when, GetterField[] getterFields, Function<TableField[], ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(f.apply($.fields(getterFields)));
    }

    private <T, R, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> R apply(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], R> f, Getter<T>... columns) {
        IDatasetField[] datasetFields = new IDatasetField[columns.length];
        for (int i = 0; i < columns.length; i++) {
            datasetFields[i] = this.$(dataset, columns[i]);
        }
        return f.apply(datasetFields);
    }


    private <R, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> R apply(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], R> f, IColumnField... columnFields) {
        IDatasetField[] datasetFields = new IDatasetField[columnFields.length];
        for (int i = 0; i < columnFields.length; i++) {
            IColumnField columnField = columnFields[i];
            if (columnField instanceof ColumnField) {
                datasetFields[i] = this.$(dataset, ((ColumnField) columnField).getColumnName());
            } else if (columnField instanceof GetterField) {
                datasetFields[i] = this.$(dataset, ((GetterField<?>) columnField).getGetter());
            } else {
                throw new RuntimeException("Not Supported");
            }
        }
        return f.apply(datasetFields);
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(this.apply(dataset, f, columnFields));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(this.apply(dataset, f, columnFields));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingAnd(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, String columnName, Function<DATASET_FIELD, ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(f.apply(this.$(dataset, columnName)));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, String columnName, Function<DATASET_FIELD, ICondition> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(f.apply(this.$(dataset, columnName)));
    }

    @Override
    public OrderBy $orderBy() {
        if (orderBy == null) {
            orderBy = new OrderBy();
            this.append(orderBy);
        }
        return orderBy;
    }

    @Override
    public IOrderByDirection ascOrderByDirection() {
        return OrderByDirection.ASC;
    }

    @Override
    public IOrderByDirection descOrderByDirection() {
        return OrderByDirection.DESC;
    }

    @Override
    public ForUpdate $forUpdate() {
        if (forUpdate == null) {
            forUpdate = new ForUpdate();
            this.append(forUpdate);
        }
        return forUpdate;
    }

    @Override
    public Limit $limit() {
        if (this.limit == null) {
            this.limit = new Limit(0, 0);
            this.append(this.limit);
        }
        return this.limit;
    }

    /**
     * orderBy 列
     *
     * @param column 列
     * @param storey 列存储层级
     * @param <T>    列的实体类
     * @return 自己
     */
    @Override
    public <T> SELF orderBy(IOrderByDirection orderByDirection, Getter<T> column, int storey) {
        return this.orderBy(orderByDirection, $.field(column, storey));
    }


    /**
     * orderBy 列
     *
     * @param column 列
     * @param storey 列存储层级
     * @param <T>    列的实体类
     * @param f      转换函数
     * @return 自己
     */
    @Override
    public <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Getter<T> column, int storey, Function<TableField, Cmd> f) {
        return this.orderBy(orderByDirection, f.apply($.field(column, storey)));
    }


    @Override
    public <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TableField[], Cmd> f, int storey, Getter<T>... columns) {
        return this.orderBy(orderByDirection, f.apply($.fields(storey, columns)));
    }

    @Override
    public SELF orderByWithFun(IOrderByDirection orderByDirection, GetterField[] getterFields, Function<TableField[], Cmd> f) {
        return this.orderBy(orderByDirection, f.apply($.fields(getterFields)));
    }

    @Override
    public <T> SELF orderBy(IOrderByDirection orderByDirection, int storey, Getter<T>... columns) {
        return this.orderBy(orderByDirection, $.fields(storey, columns));
    }

    @Override
    public SELF orderBy(IOrderByDirection orderByDirection, String columnName) {
        return this.orderBy(orderByDirection, $.column(columnName));
    }

    @Override
    public SELF orderByWithFun(IOrderByDirection orderByDirection, String columnName, Function<IDatasetField, Cmd> f) {
        return this.orderBy(orderByDirection, f.apply(this.$().column(columnName)));
    }

    /**
     * orderBy 子查询 列
     *
     * @param dataset 子查询
     * @param column  列
     * @param <T>     列的实体类
     * @return
     */
    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T> column) {
        return this.orderBy(orderByDirection, this.$(dataset, column));
    }

    /**
     * orderBy 子查询 列
     *
     * @param dataset 子查询
     * @param column  列
     * @param f       转换函数
     * @param <T>     列的实体类
     * @return
     */
    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        return this.orderBy(orderByDirection, f.apply(this.$(dataset, column)));
    }

    /**
     * orderBy 子查询 列
     *
     * @param dataset    子查询
     * @param columnName 列
     * @param f          转换函数
     * @return
     */
    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, String columnName, Function<DATASET_FIELD, Cmd> f) {
        return this.orderBy(orderByDirection, f.apply(this.$(dataset, columnName)));
    }


    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return this.orderBy(orderByDirection, this.apply(dataset, f, columns));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return this.orderBy(orderByDirection, this.apply(dataset, f, columnFields));
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, String columnName) {
        return this.orderBy(orderByDirection, this.$(dataset, columnName));
    }


    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        return this.orderBy(orderByDirection, f.apply(this.$(dataset, column)));
    }


    public Unions $unions() {
        if (this.unions == null) {
            this.unions = new Unions();
            this.cmds.add(unions);
        }
        return this.unions;
    }

    @Override
    public SELF union(IQuery unionQuery) {
        $unions().add(new Union(unionQuery));
        return (SELF) this;
    }

    @Override
    public SELF unionAll(IQuery unionQuery) {
        $unions().add(new Union(SqlConst.UNION_ALL, unionQuery));
        return (SELF) this;
    }


    @Override
    public Select getSelect() {
        return this.select;
    }

    @Override
    public From getFrom() {
        return this.from;
    }

    @Override
    public Joins getJoins() {
        return this.joins;
    }

    @Override
    public Where getWhere() {
        return this.where;
    }

    @Override
    public GroupBy getGroupBy() {
        return this.groupBy;
    }

    @Override
    public OrderBy getOrderBy() {
        return this.orderBy;
    }

    @Override
    public Limit getLimit() {
        return this.limit;
    }

    @Override
    public Unions getUnions() {
        return this.unions;
    }

    @Override
    public ForUpdate getForUpdate() {
        return forUpdate;
    }
}

