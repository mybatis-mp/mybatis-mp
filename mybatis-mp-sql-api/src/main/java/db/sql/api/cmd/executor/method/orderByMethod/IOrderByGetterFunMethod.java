package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IOrderByGetterFunMethod<SELF extends IOrderByGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IBaseOrderByMethods {

    default <T> SELF orderByWithFun(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.orderByWithFun(ascOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderByWithFun(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.orderByWithFun(ascOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderByDescWithFun(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.orderByWithFun(descOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderByDescWithFun(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.orderByWithFun(descOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderByWithFun(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderByWithFun(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderByDescWithFun(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderByDescWithFun(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.orderByWithFun(orderByDirection, column, 1, f);
    }

    <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f);

    default <T> SELF orderBy(boolean when, IOrderByDirection orderByDirection, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, column, 1, f);
    }

    default <T> SELF orderBy(boolean when, IOrderByDirection orderByDirection, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, column, storey, f);
    }
}
