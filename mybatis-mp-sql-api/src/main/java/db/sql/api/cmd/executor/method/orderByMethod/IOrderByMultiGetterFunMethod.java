package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
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

    default SELF orderByWithFun(Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        return this.orderByWithFun(ascOrderByDirection(), f, getterFields);
    }

    default SELF orderByDescWithFun(Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        return this.orderByWithFun(descOrderByDirection(), f, getterFields);
    }

    default SELF orderByWithFun(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        return this.orderByWithFun(ascOrderByDirection(), f, getterFields);
    }

    default SELF orderByDescWithFun(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        return this.orderByWithFun(descOrderByDirection(), f, getterFields);
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

    default SELF orderByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), f, getterFields);
    }

    default SELF orderByDescWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), f, getterFields);
    }

    default SELF orderByWithFun(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(ascOrderByDirection(), getterFields, f);
    }

    default SELF orderByDescWithFun(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(descOrderByDirection(), getterFields, f);
    }

    default <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(orderByDirection, f, 1, columns);
    }

    <T> SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns);

    default SELF orderByWithFun(IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        return this.orderByWithFun(orderByDirection, getterFields, f);
    }

    SELF orderByWithFun(IOrderByDirection orderByDirection, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f);

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


    default SELF orderByWithFun(boolean when, IOrderByDirection orderByDirection, Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, f, getterFields);
    }

    default SELF orderByWithFun(boolean when, IOrderByDirection orderByDirection, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(orderByDirection, getterFields, f);
    }
}
