package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IOrderByDirection;


public interface IOrderByDatasetMultiGetterMethod<SELF extends IOrderByDatasetMultiGetterMethod> extends IBaseOrderByMethods {

    default <T> SELF orderBy(IDataset dataset, Getter<T>... columns) {
        return this.orderBy(dataset, ascOrderByDirection(), columns);
    }

    default <T> SELF orderByDesc(IDataset dataset, Getter<T>... columns) {
        return this.orderBy(dataset, descOrderByDirection(), columns);
    }

    default <T> SELF orderBy(boolean when, IDataset dataset, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, ascOrderByDirection(), columns);
    }

    default <T> SELF orderByDesc(boolean when, IDataset dataset, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, descOrderByDirection(), columns);
    }

    <T> SELF orderBy(IDataset dataset, IOrderByDirection orderByDirection, Getter<T>... columns);

    default <T> SELF orderBy(boolean when, IDataset dataset, IOrderByDirection orderByDirection, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, orderByDirection, columns);
    }
}
