package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterColumnField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface ISelectMultiGetterFunMethod<SELF extends ISelectMultiGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF selectWithFun(Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.selectWithFun(f, 1, columns);
    }

    <T> SELF selectWithFun(Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns);


    SELF selectWithFun(Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields);

    default <T> SELF selectWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(f, 1, columns);
    }


    default <T> SELF selectWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(f, storey, columns);
    }

    default SELF selectWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterColumnField... getterColumnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(f, getterColumnFields);
    }
}
