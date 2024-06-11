package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface IGroupByDatasetMethod<SELF extends IGroupByDatasetMethod, DATASET_FILED extends Cmd> {

    SELF groupBy(IDataset Dataset, String columnName);

    SELF groupByWithFun(IDataset subQuery, String columnName, Function<DATASET_FILED, Cmd> f);

    default SELF groupBy(boolean when, IDataset dataset, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, columnName);
    }

    default SELF groupByWithFun(boolean when, IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(dataset, columnName, f);
    }
}
