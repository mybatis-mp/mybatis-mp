package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface ISelectMultiGetterMethod<SELF extends ISelectMultiGetterMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF select(Getter<T>... columns) {
        return this.select(1, columns);
    }

    <T> SELF select(int storey, Getter<T>... columns);

    default <T> SELF select(boolean when, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(1, columns);
    }

    default <T> SELF select(boolean when, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(storey, columns);
    }

    /**
     * @param getterFields 利用 GetterFields 进行 数组构建
     * @param f
     * @return
     */
    SELF select(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f);

    default SELF select(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(getterFields, f);
    }
}
