package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IOrderByMultiGetterFunMethod<SELF extends IOrderByMultiGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IBaseOrderByMethods {

    default SELF orderBy(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        return this.orderBy(ascOrderByDirection(), getterFields, f);
    }

    default SELF orderBy(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), getterFields, f);
    }

    default SELF orderByDesc(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        return this.orderBy(descOrderByDirection(), getterFields, f);
    }

    default SELF orderByDesc(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), getterFields, f);
    }

    SELF orderBy(IOrderByDirection orderByDirection, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f);

    default SELF orderBy(boolean when, IOrderByDirection orderByDirection, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, getterFields, f);
    }
}
