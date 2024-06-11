package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IOrderByDatasetMethod<SELF extends IOrderByDatasetMethod, DATASET_FILED extends Cmd> extends IBaseOrderByMethods {

    default SELF orderBy(IDataset dataset, String columnName) {
        return this.orderBy(dataset, ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(IDataset dataset, String columnName) {
        return this.orderBy(dataset, descOrderByDirection(), columnName);
    }

    default SELF orderByWithFun(IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f) {
        return this.orderByWithFun(dataset, ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDescWithFun(IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f) {
        return this.orderByWithFun(dataset, descOrderByDirection(), columnName, f);
    }

    default SELF orderBy(boolean when, IDataset dataset, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(boolean when, IDataset dataset, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, descOrderByDirection(), columnName);
    }

    default SELF orderByWithFun(boolean when, IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDescWithFun(boolean when, IDataset dataset, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, descOrderByDirection(), columnName, f);
    }

    SELF orderBy(IDataset dataset, IOrderByDirection orderByDirection, String columnName);

    SELF orderByWithFun(IDataset dataset, IOrderByDirection orderByDirection, String columnName, Function<DATASET_FILED, Cmd> f);

    default SELF orderBy(boolean when, IDataset dataset, IOrderByDirection orderByDirection, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, orderByDirection, columnName);
    }

    default SELF orderByWithFun(boolean when, IDataset dataset, IOrderByDirection orderByDirection, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, orderByDirection, columnName, f);
    }
}
