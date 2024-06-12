package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface IGroupByDatasetMultiGetterFunMethod<SELF extends IGroupByDatasetMultiGetterFunMethod> {

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns);

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, f, columns);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, f, columnFields);
    }
}
