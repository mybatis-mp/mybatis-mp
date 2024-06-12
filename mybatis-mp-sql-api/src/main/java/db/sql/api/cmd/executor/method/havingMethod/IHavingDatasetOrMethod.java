package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface IHavingDatasetOrMethod<SELF extends IHavingDatasetOrMethod> {

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, ICondition> f) {
        return this.havingOr(dataset, true, columnName, f);
    }

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, String columnName, Function<DATASET_FIELD, ICondition> f);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, ICondition> f) {
        return havingOr(dataset, true, column, f);
    }

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Getter<T> column, Function<DATASET_FIELD, ICondition> f);


    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, Getter<T>... columns) {
        return this.havingOr(dataset, true, f, columns);
    }

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, Getter<T>... columns);


    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], ICondition> f, IColumnField... columnFields) {
        return this.havingOr(dataset, true, f, columnFields);
    }

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF havingOr(IDataset<DATASET, DATASET_FIELD> dataset, boolean when, Function<IDatasetField[], ICondition> f, IColumnField... columnFields);
}
