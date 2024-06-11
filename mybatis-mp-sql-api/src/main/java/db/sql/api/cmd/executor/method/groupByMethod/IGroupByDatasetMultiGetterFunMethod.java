package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface IGroupByDatasetMultiGetterFunMethod<SELF extends IGroupByDatasetMultiGetterFunMethod, DATASET_FILED extends Cmd> {

    <T> SELF groupByWithFun(IDataset Dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns);

    SELF groupByWithFun(IDataset subQuery, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields);

    default <T> SELF groupByWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, f, columns);
    }

    default SELF groupByWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, f, columnFields);
    }
}
