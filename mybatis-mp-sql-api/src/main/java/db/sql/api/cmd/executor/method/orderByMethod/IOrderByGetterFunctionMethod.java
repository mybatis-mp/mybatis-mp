package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IOrderByGetterFunctionMethod<SELF extends IOrderByGetterFunctionMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IBaseOrderByMethods, IOrderByGetterFunMethod<SELF, TABLE, TABLE_FIELD> {

    default <T> SELF orderBy(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.orderBy(ascOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderBy(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.orderBy(ascOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderByDesc(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.orderBy(descOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderByDesc(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.orderBy(descOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderBy(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderBy(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderByDesc(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), column, 1, f);
    }

    default <T> SELF orderByDesc(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), column, storey, f);
    }

    default <T> SELF orderBy(IOrderByDirection orderByDirection, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.orderBy(orderByDirection, column, 1, f);
    }

    default <T> SELF orderBy(IOrderByDirection orderByDirection, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.orderByWithFun(orderByDirection, column, storey, f);
    }
}
