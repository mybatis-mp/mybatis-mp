package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IGroupByMultiGetterFunMethod<SELF extends IGroupByMultiGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF groupByWithFun(Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.groupByWithFun(f, 1, columns);
    }

    <T> SELF groupByWithFun(Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns);


    SELF groupByWithFun(Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields);

    default <T> SELF groupByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(f, 1, columns);
    }


    default <T> SELF groupByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(f, storey, columns);
    }

    default SELF groupByWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupByWithFun(f, getterFields);
    }
}
