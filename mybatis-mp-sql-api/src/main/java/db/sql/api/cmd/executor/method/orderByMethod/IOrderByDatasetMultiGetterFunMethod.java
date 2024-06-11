package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IOrderByDatasetMultiGetterFunMethod<SELF extends IOrderByDatasetMultiGetterFunMethod, DATASET_FILED extends Cmd> extends IBaseOrderByMethods {

    default <T> SELF orderByWithFun(IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columns);
    }

    default <T> SELF orderByDescWithFun(IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columns);
    }

    default SELF orderByWithFun(IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columnFields);
    }

    default SELF orderByDescWithFun(IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columnFields);
    }

    default <T> SELF orderByWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columns);
    }

    default <T> SELF orderByDescWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columns);
    }

    default SELF orderByWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columnFields);
    }

    default SELF orderByDescWithFun(boolean when, IDataset dataset, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columnFields);
    }

    <T> SELF orderByWithFun(IDataset dataset, IOrderByDirection orderByDirection, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns);

    SELF orderByWithFun(IDataset dataset, IOrderByDirection orderByDirection, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields);

    default <T> SELF orderByWithFun(boolean when, IDataset dataset, IOrderByDirection orderByDirection, Function<DATASET_FILED[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, orderByDirection, f, columns);
    }

    default SELF orderByWithFun(boolean when, IDataset dataset, IOrderByDirection orderByDirection, Function<DATASET_FILED[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, orderByDirection, f, columnFields);
    }
}
