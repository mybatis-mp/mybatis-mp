package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IOrderByDatasetGetterMethod<SELF extends IOrderByDatasetGetterMethod, DATASET_FILED extends Cmd> extends IBaseOrderByMethods {

    default <T> SELF orderBy(IDataset dataset, Getter<T> column) {
        return this.orderBy(dataset, ascOrderByDirection(), column);
    }

    default <T> SELF orderByDesc(IDataset dataset, Getter<T> column) {
        return this.orderBy(dataset, descOrderByDirection(), column);
    }

    default <T> SELF orderBy(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        return this.orderBy(dataset, ascOrderByDirection(), column, f);
    }

    default <T> SELF orderByDesc(IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        return this.orderBy(dataset, descOrderByDirection(), column, f);
    }

    default <T> SELF orderBy(boolean when, IDataset dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, ascOrderByDirection(), column);
    }

    default <T> SELF orderByDesc(boolean when, IDataset dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, descOrderByDirection(), column);
    }

    default <T> SELF orderBy(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, ascOrderByDirection(), column, f);
    }

    default <T> SELF orderByDesc(boolean when, IDataset dataset, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, descOrderByDirection(), column, f);
    }

    <T> SELF orderBy(IDataset dataset, IOrderByDirection orderByDirection, Getter<T> column);

    <T> SELF orderBy(IDataset dataset, IOrderByDirection orderByDirection, Getter<T> column, Function<DATASET_FILED, Cmd> f);

    default <T> SELF orderBy(boolean when, IDataset dataset, IOrderByDirection orderByDirection, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, orderByDirection, column);
    }

    default <T> SELF orderBy(boolean when, IDataset dataset, IOrderByDirection orderByDirection, Getter<T> column, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, orderByDirection, column, f);
    }
}
