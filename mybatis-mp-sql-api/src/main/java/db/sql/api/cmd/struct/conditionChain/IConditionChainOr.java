package db.sql.api.cmd.struct.conditionChain;

import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ICondition;

import java.util.function.Function;

public interface IConditionChainOr<SELF extends IConditionChainOr, TABLE_FIELD> {

    SELF or();

    SELF or(ICondition condition);

    default SELF or(ICondition condition, boolean when) {
        if (!when) {
            return (SELF) this;
        }
        return this.or(condition);
    }

    default <T> SELF or(Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.or(true, column, f);
    }

    default <T> SELF or(boolean when, Getter<T> column, Function<TABLE_FIELD, ICondition> f) {
        return this.or(when, column, 1, f);
    }

    default <T> SELF or(Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f) {
        return this.or(true, column, storey, f);
    }

    <T> SELF or(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, ICondition> f);

    default SELF or(GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f) {
        return or(true, getterFields, f);
    }

    SELF or(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], ICondition> f);
}
