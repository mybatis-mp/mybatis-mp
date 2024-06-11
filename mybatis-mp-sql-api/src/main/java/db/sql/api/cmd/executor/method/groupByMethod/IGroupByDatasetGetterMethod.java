package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface IGroupByDatasetGetterMethod<SELF extends IGroupByDatasetGetterMethod, DATASET_FILED extends Cmd> {

    <T> SELF groupBy(IDataset dataset, Getter<T> column);

    <T> SELF groupBy(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f);

    default <T> SELF groupBy(boolean when, IDataset dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, column);
    }

    default <T> SELF groupBy(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, column, f);
    }
}
