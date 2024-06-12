package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface ISelectDatasetMultiGetterFunMethod<SELF extends ISelectDatasetMultiGetterFunMethod> {

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns);

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(dataset, f, columns);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF selectWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(dataset, f, columnFields);
    }
}
