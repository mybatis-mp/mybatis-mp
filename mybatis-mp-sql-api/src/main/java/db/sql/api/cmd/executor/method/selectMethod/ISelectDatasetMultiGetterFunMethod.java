package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface ISelectDatasetMultiGetterFunMethod<SELF extends ISelectDatasetMultiGetterFunMethod, DATASET_FILED extends Cmd> {

    <T> SELF selectWithFun(IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns);

    SELF selectWithFun(IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields);

    default <T> SELF selectWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(dataset, f, columns);
    }

    default SELF selectWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(dataset, f, columnFields);
    }
}
