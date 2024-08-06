package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.*;

import java.util.function.Function;

public interface IHavingMethods<SELF extends IHavingMethods,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>
        >
        extends IHavingAndMethod<SELF, TABLE, TABLE_FIELD>,
        IHavingOrMethod<SELF, TABLE, TABLE_FIELD>,
        IHavingDatasetAndMethod<SELF>,
        IHavingDatasetOrMethod<SELF> {

    default SELF having(ICondition condition) {
        return this.havingAnd(condition);
    }

    default SELF having(ICondition condition, boolean when) {
        return this.havingAnd(condition, when);
    }

    default <T> SELF having(Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(column, f);
    }

    default <T> SELF having(boolean when, Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(when, column, f);
    }

    default <T> SELF having(Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(column, storey, f);
    }

    default <T> SELF having(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(when, column, storey, f);
    }

    default <T> SELF having(Function<TABLE_FIELD[], ICondition> f, Getter<T>... columns) {
        return this.havingAnd(f, columns);
    }

    default <T> SELF having(boolean when, Function<TABLE_FIELD[], ICondition> f, Getter<T>... columns) {
        return this.havingAnd(when, f, columns);
    }

    default <T> SELF having(Function<TABLE_FIELD[], ICondition> f, int storey, Getter<T>... columns) {
        return this.havingAnd(f, storey, columns);
    }

    default <T> SELF having(boolean when, Function<TABLE_FIELD[], ICondition> f, int storey, Getter<T>... columns) {
        return this.havingAnd(when, f, storey, columns);
    }

    default SELF having(Function<TABLE_FIELD[], ICondition> f, GetterField... getterFields) {
        return this.havingAnd(f, getterFields);
    }

    default SELF having(boolean when, Function<TABLE_FIELD[], ICondition> f, GetterField... getterFields) {
        return this.havingAnd(when, f, getterFields);
    }


    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(dataset, columnName, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(dataset, when, columnName, f);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(dataset, column, f);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(dataset, when, column, f);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return this.havingAnd(dataset, f, columns);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return this.havingAnd(dataset, when, f, columns);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return this.havingAnd(dataset, f, columnFields);
    }
}
