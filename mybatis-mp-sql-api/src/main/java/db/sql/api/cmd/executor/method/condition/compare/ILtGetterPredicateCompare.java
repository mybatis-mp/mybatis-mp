package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.util.function.Predicate;

public interface ILtGetterPredicateCompare<RV, V> {

    default <T, V2> RV lt(Getter<T> column, V2 value, Predicate<V2> predicate) {
        return this.lt(column, 1, value, predicate);
    }

    default <T, V2> RV lt(Getter<T> column, int storey, V2 value, Predicate<V2> predicate) {
        return lt(predicate.test(value), column, storey, (V) value);
    }

    <T> RV lt(boolean when, Getter<T> column, int storey, V value);
}
