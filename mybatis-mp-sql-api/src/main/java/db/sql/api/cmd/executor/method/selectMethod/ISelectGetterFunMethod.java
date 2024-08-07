package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface ISelectGetterFunMethod<SELF extends ISelectGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF selectWithFun(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.selectWithFun(column, 1, f);
    }

    <T> SELF selectWithFun(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f);

    default <T> SELF selectWithFun(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(column, 1, f);
    }

    default <T> SELF selectWithFun(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(column, storey, f);
    }
}
