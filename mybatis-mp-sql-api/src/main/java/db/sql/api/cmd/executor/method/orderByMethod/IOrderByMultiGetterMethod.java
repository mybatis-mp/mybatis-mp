package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

public interface IOrderByMultiGetterMethod<SELF extends IOrderByMultiGetterMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IOrderByMultiGetterFunMethod<SELF, TABLE, TABLE_FIELD> {

    default <T> SELF orderBy(Getter<T>... columns) {
        return this.orderBy(ascOrderByDirection(), 1, columns);
    }

    default <T> SELF orderBy(int storey, Getter<T>... columns) {
        return this.orderBy(ascOrderByDirection(), storey, columns);
    }

    default <T> SELF orderByDesc(Getter<T>... columns) {
        return this.orderBy(descOrderByDirection(), 1, columns);
    }

    default <T> SELF orderByDesc(int storey, Getter<T>... columns) {
        return this.orderBy(descOrderByDirection(), storey, columns);
    }

    default <T> SELF orderBy(boolean when, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), 1, columns);
    }

    default <T> SELF orderBy(boolean when, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), storey, columns);
    }

    default <T> SELF orderByDesc(boolean when, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), 1, columns);
    }

    default <T> SELF orderByDesc(boolean when, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), storey, columns);
    }

    default <T> SELF orderBy(IOrderByDirection orderByDirection, Getter<T>... columns) {
        return this.orderBy(orderByDirection, 1, columns);
    }

    <T> SELF orderBy(IOrderByDirection orderByDirection, int storey, Getter<T>... columns);


    default <T> SELF orderBy(boolean when, IOrderByDirection orderByDirection, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, 1, columns);
    }

    default <T> SELF orderBy(boolean when, IOrderByDirection orderByDirection, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, storey, columns);
    }
}
