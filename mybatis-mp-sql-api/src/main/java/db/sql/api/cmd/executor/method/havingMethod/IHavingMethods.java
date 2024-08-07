package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.*;

import java.util.function.Function;

public interface IHavingMethods<SELF extends IHavingMethods,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>
        >
        extends IHavingAndMethod<SELF, TABLE, TABLE_FIELD>,
        IHavingDatasetAndMethod<SELF>,

        IHavingOrMethod<SELF, TABLE, TABLE_FIELD>,
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

    default SELF having(GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f) {
        return this.havingAnd(getterFields, f);
    }

    default SELF having(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f) {
        return this.havingAnd(when, getterFields, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(dataset, columnName, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(when, dataset, columnName, f);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(dataset, column, f);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        return this.havingAnd(when, dataset, column, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], ICondition> f) {
        return this.havingAnd(dataset, getterFields, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF having(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], ICondition> f) {
        return this.havingAnd(when, dataset, getterFields, f);
    }
}
