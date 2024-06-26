package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface IGroupByDatasetGetterFunMethod<SELF extends IGroupByDatasetGetterFunMethod> {

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, column, f);
    }
}
