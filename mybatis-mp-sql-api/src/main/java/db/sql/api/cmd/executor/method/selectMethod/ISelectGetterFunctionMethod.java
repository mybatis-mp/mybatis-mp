package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface ISelectGetterFunctionMethod<SELF extends ISelectGetterFunctionMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends ISelectGetterFunMethod<SELF, TABLE, TABLE_FIELD> {

    default <T> SELF select(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.select(column, 1, f);
    }

    default <T> SELF select(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.selectWithFun(column, storey, f);
    }

    default <T> SELF select(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(column, 1, f);
    }

    default <T> SELF select(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(column, storey, f);
    }
}
