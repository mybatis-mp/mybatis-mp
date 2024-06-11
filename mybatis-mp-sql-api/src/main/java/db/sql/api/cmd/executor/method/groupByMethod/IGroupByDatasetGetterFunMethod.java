package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IGroupByDatasetGetterFunMethod<SELF extends IGroupByDatasetGetterFunMethod, DATASET_FILED extends Cmd> {

    <T> SELF groupByWithFun(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f);

    default <T> SELF groupByWithFun(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, column, f);
    }
}
