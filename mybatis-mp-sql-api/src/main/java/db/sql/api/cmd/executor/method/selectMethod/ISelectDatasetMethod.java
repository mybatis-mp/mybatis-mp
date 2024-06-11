package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface ISelectDatasetMethod<SELF extends ISelectDatasetMethod, DATASET_FILED extends Cmd> {

    SELF select(IDataset dataset, String columnName);

    SELF selectWithFun(IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f);

    default SELF select(boolean when, IDataset dataset, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(dataset, columnName);
    }

    default SELF selectWithFun(boolean when, IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(dataset, columnName, f);
    }
}
