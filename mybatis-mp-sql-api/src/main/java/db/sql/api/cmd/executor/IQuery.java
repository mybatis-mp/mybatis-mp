package db.sql.api.cmd.executor;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.ICmdFactory;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.*;
import db.sql.api.cmd.executor.method.*;
import db.sql.api.cmd.struct.*;
import db.sql.api.cmd.struct.conditionChain.IConditionChain;
import db.sql.api.cmd.struct.query.*;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IQuery<SELF extends IQuery
        , TABLE extends ITable<TABLE, TABLE_FIELD>
        , TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>
        , COLUMN extends Cmd,
        V,

        CMD_FACTORY extends ICmdFactory<TABLE, TABLE_FIELD>,
        CONDITION_CHAIN extends IConditionChain<CONDITION_CHAIN, TABLE_FIELD, COLUMN, V>,

        WITH extends IWith<WITH>,
        SELECT extends ISelect<SELECT>,
        FROM extends IFrom,
        JOIN extends IJoin<JOIN, ON, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        ON extends IOn<ON, JOIN, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        JOINS extends Joins<JOIN>,
        WHERE extends IWhere<WHERE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        GROUPBY extends IGroupBy<GROUPBY, COLUMN>,
        HAVING extends IHaving<HAVING>,
        ORDERBY extends IOrderBy<ORDERBY>,
        LIMIT extends ILimit<LIMIT>,
        FORUPDATE extends IForUpdate<FORUPDATE>,
        IUNION extends IUnion
        >
        extends IWithMethod<SELF>,
        ISelectMethod<SELF, TABLE, TABLE_FIELD, COLUMN>,
        IFromMethod<SELF, TABLE, TABLE_FIELD>,
        IJoinMethod<SELF, ON>,
        IWhereMethod<SELF, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        IGroupByMethod<SELF, TABLE, TABLE_FIELD, COLUMN>,
        IHavingMethod<SELF, TABLE, TABLE_FIELD, HAVING>,
        IOrderByMethod<SELF, TABLE, TABLE_FIELD, COLUMN>,
        ILimitMethod<SELF>,
        IForUpdateMethod<SELF>,
        IUnionMethod<SELF>,
        IExecutor<SELF, TABLE, TABLE_FIELD> {

    CMD_FACTORY $();

    WITH $with(IWithQuery withQuery);

    SELECT $select();

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> FROM $from(IDataset<DATASET, DATASET_FIELD>... tables);

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> JOIN $join(JoinMode mode, DATASET mainTable, DATASET secondTable);

    WHERE $where();

    GROUPBY $groupBy();

    HAVING $having();

    ORDERBY $orderBy();

    LIMIT $limit();

    FORUPDATE $forUpdate();

    @Override
    default SELF with(IWithQuery withQuery) {
        $with(withQuery);
        return (SELF) this;
    }

    @Override
    default SELF select(Cmd column) {
        $select().select(column);
        return (SELF) this;
    }

    @Override
    default SELF select(Class entity, int storey) {
        return this.select($().allField($().table(entity, storey)));
    }

    @Override
    default SELF selectDistinct() {
        $select().distinct();
        return (SELF) this;
    }

    @Override
    default <T> SELF selectIgnore(Getter<T> column, int storey) {
        this.$select().selectIgnore($().field(column, storey));
        return (SELF) this;
    }

    @Override
    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF from(IDataset<DATASET, DATASET_FIELD>... tables) {
        $from(tables);
        return (SELF) this;
    }

    default SELF join(Class mainTable, Class secondTable, BiConsumer<TABLE, ON> consumer) {
        return this.join(JoinMode.INNER, mainTable, secondTable, consumer);
    }

    default SELF join(JoinMode mode, Class mainTable, Class secondTable, BiConsumer<TABLE, ON> consumer) {
        return this.join(mode, mainTable, 1, secondTable, 1, consumer);
    }

    SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, BiConsumer<TABLE, ON> consumer);

    default SELF where(Consumer<WHERE> whereConsumer) {
        whereConsumer.accept($where());
        return (SELF) this;
    }

    @Override
    default SELF groupBy(COLUMN column) {
        $groupBy().groupBy(column);
        return (SELF) this;
    }

    @Override
    default SELF groupBy(COLUMN... columns) {
        $groupBy().groupBy(columns);
        return (SELF) this;
    }

    @Override
    default SELF groupBy(List<COLUMN> columns) {
        $groupBy().groupBy(columns);
        return (SELF) this;
    }

    @Override
    default SELF having(Consumer<HAVING> consumer) {
        consumer.accept($having());
        return (SELF) this;
    }

    @Override
    default SELF havingAnd(ICondition condition) {
        $having().and(condition);
        return (SELF) this;
    }

    @Override
    default SELF havingOr(ICondition condition) {
        $having().or(condition);
        return (SELF) this;
    }

    @Override
    default SELF orderBy(IOrderByDirection orderByDirection, Cmd column) {
        $orderBy().orderBy(orderByDirection, column);
        return (SELF) this;
    }

    @Override
    default SELF limit(int limit) {
        return this.limit(0, limit);
    }

    @Override
    default SELF limit(int offset, int limit) {
        $limit().set(offset, limit);
        return (SELF) this;
    }


    @Override
    default SELF forUpdate(boolean wait) {
        $forUpdate().setWait(wait);
        return (SELF) this;
    }

    SELECT getSelect();

    WHERE getWhere();

    FROM getFrom();

    JOINS getJoins();

    GROUPBY getGroupBy();

    ORDERBY getOrderBy();

    LIMIT getLimit();

    FORUPDATE getForUpdate();

    Unions getUnions();

    @Override
    default CONDITION_CHAIN conditionChain() {
        return $where().conditionChain();
    }
}
