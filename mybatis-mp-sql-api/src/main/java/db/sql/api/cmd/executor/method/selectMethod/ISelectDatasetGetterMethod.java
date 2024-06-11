package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface ISelectDatasetGetterMethod<SELF extends ISelectDatasetGetterMethod, DATASET_FILED extends Cmd> {

    <T> SELF select(IDataset dataset, Getter<T> column);

    <T> SELF select(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f);

    default <T> SELF select(boolean when, IDataset dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(dataset, column);
    }

    default <T> SELF select(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(dataset, column, f);
    }
}
