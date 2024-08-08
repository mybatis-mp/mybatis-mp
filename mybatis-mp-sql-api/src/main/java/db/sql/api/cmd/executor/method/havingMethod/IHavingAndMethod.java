package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IHavingAndMethod<SELF extends IHavingAndMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    SELF havingAnd(ICondition condition);

    default SELF havingAnd(ICondition condition, boolean when) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingAnd(condition);
    }

    //---

    default <T> SELF havingAnd(Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(true, column, f);
    }

    default <T> SELF havingAnd(boolean when, Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(when, column, 1, f);
    }

    default <T> SELF havingAnd(Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f) {
        return this.havingAnd(true, column, storey, f);
    }

    <T> SELF havingAnd(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f);

    default SELF havingAnd(GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f) {
        return this.havingAnd(true, getterFields, f);
    }

    SELF havingAnd(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f);
}
