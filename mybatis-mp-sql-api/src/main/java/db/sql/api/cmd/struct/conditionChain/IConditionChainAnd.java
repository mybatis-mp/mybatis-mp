package db.sql.api.cmd.struct.conditionChain;

import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ICondition;

import java.util.function.Function;

public interface IConditionChainAnd<SELF extends IConditionChainAnd, TABLE_FIELD> {

    SELF and();

    SELF and(ICondition condition);

    default SELF and(ICondition condition, boolean when) {
        if (!when) {
            return (SELF) this;
        }
        return this.and(condition);
    }

    default <T> SELF and(Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.and(true, column, f);
    }

    default <T> SELF and(boolean when, Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.and(when, column, 1, f);
    }

    default <T> SELF and(Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f) {
        return this.and(true, column, storey, f);
    }

    <T> SELF and(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f);

    default SELF and(GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f) {
        return and(true, getterFields, f);
    }


    SELF and(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f);
}
