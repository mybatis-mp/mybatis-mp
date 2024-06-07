package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IOrderBySubQueryMethod<SELF extends IOrderBySubQueryMethod, DATASET_FILED extends Cmd> extends IBaseOrderByMethods {

    default SELF orderBy(ISubQuery subQuery, String columnName) {
        return this.orderBy(subQuery, ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(ISubQuery subQuery, String columnName) {
        return this.orderBy(subQuery, descOrderByDirection(), columnName);
    }

    default SELF orderByWithFun(ISubQuery subQuery, String columnName, Function<DATASET_FILED, Cmd> f) {
        return this.orderByWithFun(subQuery, ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDescWithFun(ISubQuery subQuery, String columnName, Function<DATASET_FILED, Cmd> f) {
        return this.orderByWithFun(subQuery, descOrderByDirection(), columnName, f);
    }

    default SELF orderBy(boolean when, ISubQuery subQuery, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(subQuery, ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(boolean when, ISubQuery subQuery, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(subQuery, descOrderByDirection(), columnName);
    }

    default SELF orderByWithFun(boolean when, ISubQuery subQuery, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(subQuery, ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDescWithFun(boolean when, ISubQuery subQuery, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(subQuery, descOrderByDirection(), columnName, f);
    }

    SELF orderBy(ISubQuery subQuery, IOrderByDirection orderByDirection, String columnName);

    SELF orderByWithFun(ISubQuery subQuery, IOrderByDirection orderByDirection, String columnName, Function<DATASET_FILED, Cmd> f);

    default SELF orderBy(boolean when, ISubQuery subQuery, IOrderByDirection orderByDirection, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(subQuery, orderByDirection, columnName);
    }

    default SELF orderByWithFun(boolean when, ISubQuery subQuery, IOrderByDirection orderByDirection, String columnName, Function<DATASET_FILED, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(subQuery, orderByDirection, columnName, f);
    }
}
