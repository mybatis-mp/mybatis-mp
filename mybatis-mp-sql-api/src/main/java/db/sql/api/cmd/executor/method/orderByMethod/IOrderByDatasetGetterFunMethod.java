package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IOrderByDatasetGetterFunMethod<SELF extends IOrderByDatasetGetterFunMethod, DATASET_FILED extends Cmd> extends IBaseOrderByMethods {

    default <T> SELF orderByWithFun(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        return this.orderByWithFun(dataset, ascOrderByDirection(), column, f);
    }

    default <T> SELF orderByDescWithFun(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        return this.orderByWithFun(dataset, descOrderByDirection(), column, f);
    }

    default <T> SELF orderByWithFun(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, ascOrderByDirection(), column, f);
    }

    default <T> SELF orderByDescWithFun(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, descOrderByDirection(), column, f);
    }

    <T> SELF orderByWithFun(IDataset dataset, IOrderByDirection orderByDirection, Getter<T> column, Function<DATASET_FILED, Cmd> f);

    default <T> SELF orderByWithFun(boolean when, IDataset dataset, IOrderByDirection orderByDirection, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, orderByDirection, column, f);
    }
}
