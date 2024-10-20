package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

public interface IOrderByGetterMethod<SELF extends IOrderByGetterMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IOrderByGetterFunMethod<SELF, TABLE, TABLE_FIELD> {

    default <T> SELF orderBy(Getter<T> column) {
        return this.orderBy(ascOrderByDirection(), column, 1);
    }

    default <T> SELF orderBy(Getter<T> column, int storey) {
        return this.orderBy(ascOrderByDirection(), column, storey);
    }

    default <T> SELF orderByDesc(Getter<T> column) {
        return this.orderBy(descOrderByDirection(), column, 1);
    }

    default <T> SELF orderByDesc(Getter<T> column, int storey) {
        return this.orderBy(descOrderByDirection(), column, storey);
    }

    default <T> SELF orderBy(boolean when, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), column, 1);
    }

    default <T> SELF orderBy(boolean when, Getter<T> column, int storey) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), column, storey);
    }

    default <T> SELF orderByDesc(boolean when, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), column, 1);
    }

    default <T> SELF orderByDesc(boolean when, Getter<T> column, int storey) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), column, storey);
    }

    default <T> SELF orderBy(IOrderByDirection orderByDirection, Getter<T> column) {
        return this.orderBy(orderByDirection, column, 1);
    }

    default <T> SELF orderBy(IOrderByDirection orderByDirection, Getter<T> column, int storey) {
        return this.orderBy(orderByDirection, column, storey, null);
    }

    default <T> SELF orderBy(boolean when, IOrderByDirection orderByDirection, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, column, 1);
    }

    default <T> SELF orderBy(boolean when, IOrderByDirection orderByDirection, Getter<T> column, int storey) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, column, storey);
    }
}
