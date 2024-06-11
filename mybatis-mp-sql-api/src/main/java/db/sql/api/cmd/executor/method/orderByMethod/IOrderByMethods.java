package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IColumn;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IOrderByDirection;

import java.util.function.Function;

public interface IOrderByMethods<SELF extends IOrderByMethods,
        TABLE_FIELD extends Cmd,
        DATASET_FILED extends Cmd,
        COLUMN extends Cmd>
        extends IBaseOrderByMethods,
        IOrderByCmdMethod<SELF, COLUMN>,
        IOrderByGetterMethod<SELF>,
        IOrderByGetterFunMethod<SELF, TABLE_FIELD>,
        IOrderByMultiGetterMethod<SELF>,
        IOrderByMultiGetterFunMethod<SELF, TABLE_FIELD>,
        IOrderByDatasetMethod<SELF, DATASET_FILED>,
        IOrderByDatasetGetterMethod<SELF, DATASET_FILED>,
        IOrderByDatasetGetterFunMethod<SELF, DATASET_FILED>,
        IOrderByDatasetMultiGetterMethod<SELF>,
        IOrderByDatasetMultiGetterFunMethod<SELF, DATASET_FILED> {




    default SELF orderBy(String columnName) {
        return this.orderBy(ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(String columnName) {
        return this.orderBy(descOrderByDirection(), columnName);
    }

    default SELF orderBy(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), columnName);
    }

    default SELF orderByWithFun(String columnName, Function<IColumn, Cmd> f) {
        return this.orderByWithFun(ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDescWithFun(String columnName, Function<IColumn, Cmd> f) {
        return this.orderByWithFun(descOrderByDirection(), columnName, f);
    }

    default SELF orderByWithFun(boolean when, String columnName, Function<IColumn, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDescWithFun(boolean when, String columnName, Function<IColumn, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), columnName, f);
    }

    SELF orderBy(IOrderByDirection orderByDirection, String columnName);

    SELF orderByWithFun(IOrderByDirection orderByDirection, String columnName, Function<IColumn, Cmd> f);

    @Override
    default <T> SELF orderBy(IDataset dataset, IOrderByDirection orderByDirection, Getter<T>... columns) {
        for (Getter<T> column : columns) {
            this.orderBy(dataset, orderByDirection, column);
        }
        return (SELF) this;
    }

    default SELF orderBy(boolean when, IOrderByDirection orderByDirection, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, columnName);
    }

    default SELF orderByWithFun(boolean when, IOrderByDirection orderByDirection, String columnName, Function<IColumn, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, columnName, f);
    }
}
