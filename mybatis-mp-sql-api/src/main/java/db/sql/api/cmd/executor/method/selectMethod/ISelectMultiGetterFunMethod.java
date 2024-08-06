package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface ISelectMultiGetterFunMethod<SELF extends ISelectMultiGetterFunMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF selectWithFun(Function<TABLE_FIELD[], Cmd> f, Getter<T>... columns) {
        return this.selectWithFun(f, 1, columns);
    }

    <T> SELF selectWithFun(Function<TABLE_FIELD[], Cmd> f, int storey, Getter<T>... columns);

    default SELF selectWithFun(Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        return this.selectWithFun(getterFields, f);
    }

    /**
     * @param getterFields 利用 GetterFields 进行 数组构建
     * @param f
     * @return
     */
    SELF selectWithFun(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f);

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

    default SELF selectWithFun(boolean when, Function<TABLE_FIELD[], Cmd> f, GetterField... getterFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(f, getterFields);
    }
}
