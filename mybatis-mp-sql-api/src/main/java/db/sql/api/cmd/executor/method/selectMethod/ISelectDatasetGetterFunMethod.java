package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface ISelectDatasetGetterFunMethod<SELF extends ISelectDatasetGetterFunMethod, DATASET_FILED extends Cmd> {

    <T> SELF selectWithFun(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f);

    default <T> SELF selectWithFun(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(dataset, column, f);
    }
}
