package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IColumn;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

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
        IOrderBySubQueryMethod<SELF, DATASET_FILED>,
        IOrderBySubQueryGetterMethod<SELF, DATASET_FILED>,
        IOrderBySubQueryGetterFunMethod<SELF, DATASET_FILED>,
        IOrderBySubQueryMultiGetterMethod<SELF>,
        IOrderBySubQueryMultiGetterFunMethod<SELF, DATASET_FILED> {




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
    default <T> SELF orderBy(ISubQuery subQuery, IOrderByDirection orderByDirection, Getter<T>... columns) {
        for (Getter<T> column : columns) {
            this.orderBy(subQuery, orderByDirection, column);
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
