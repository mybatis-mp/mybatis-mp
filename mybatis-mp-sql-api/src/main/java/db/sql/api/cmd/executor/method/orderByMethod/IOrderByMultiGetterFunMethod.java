package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterColumnField;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IOrderByMultiGetterFunMethod<SELF extends IOrderByMultiGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IBaseOrderByMethods {

    default <T> SELF orderByWithFun(Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(ascOrderByDirection(), f, 1, columns);
    }

    default <T> SELF orderByWithFun(Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        return this.orderByWithFun(ascOrderByDirection(), f, storey, columns);
    }

    default <T> SELF orderByDescWithFun(Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(descOrderByDirection(), f, 1, columns);
    }

    default <T> SELF orderByDescWithFun(Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        return this.orderByWithFun(descOrderByDirection(), f, storey, columns);
    }

    default SELF orderByWithFun(Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields) {
        return this.orderByWithFun(ascOrderByDirection(), f, getterColumnFields);
    }

    default SELF orderByDescWithFun(Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields) {
        return this.orderByWithFun(descOrderByDirection(), f, getterColumnFields);
    }

    default <T> SELF orderByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), f, 1, columns);
    }

    default <T> SELF orderByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), f, storey, columns);
    }

    default <T> SELF orderByDescWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), f, 1, columns);
    }

    default <T> SELF orderByDescWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), f, storey, columns);
    }

    default SELF orderByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), f, getterColumnFields);
    }

    default SELF orderByDescWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), f, getterColumnFields);
    }

    default <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(orderByDirection, f, 1, columns);
    }

    <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns);

    SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields);

    default <T> SELF orderByWithFun(boolean when, IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, f, 1, columns);
    }

    default <T> SELF orderByWithFun(boolean when, IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, f, storey, columns);
    }

    default SELF orderByWithFun(boolean when, IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, f, getterColumnFields);
    }
}
