package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IHavingOrMethod<SELF extends IHavingOrMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    SELF havingOr(ICondition condition);

    default SELF havingOr(ICondition condition, boolean when) {
        if (!when) {
            return (SELF) this;
        }
        return this.havingOr(condition);
    }

    default <T> SELF havingOr(Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.havingOr(true, column, f);
    }

    default <T> SELF havingOr(boolean when, Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.havingOr(when, column, 1, f);
    }

    default <T> SELF havingOr(Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f) {
        return this.havingOr(true, column, storey, f);
    }

    <T> SELF havingOr(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f);

    default <T> SELF havingOr(Function<TABLE_FIELD[], ICondition> f, Getter<T>... columns) {
        return this.havingOr(true, f, columns);
    }

    default <T> SELF havingOr(boolean when, Function<TABLE_FIELD[], ICondition> f, Getter<T>... columns) {
        return this.havingOr(when, f, 1, columns);
    }

    default <T> SELF havingOr(Function<TABLE_FIELD[], ICondition> f, int storey, Getter<T>... columns) {
        return this.havingOr(true, f, storey, columns);
    }

    <T> SELF havingOr(boolean when, Function<TABLE_FIELD[], ICondition> f, int storey, Getter<T>... columns);

    default SELF havingOr(Function<TABLE_FIELD[], ICondition> f, GetterField... getterFields) {
        return this.havingOr(true, f, getterFields);
    }

    SELF havingOr(boolean when, Function<TABLE_FIELD[], ICondition> f, GetterField... getterFields);

}
